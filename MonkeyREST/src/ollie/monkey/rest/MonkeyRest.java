package ollie.monkey.rest;

import static spark.Spark.*;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import monkeyboard.api.KeyStoneAPI;
import monkeyboard.api.RadioMode;

public class MonkeyRest {

	private static KeyStoneAPI api = new KeyStoneAPI();
	
	public static void main(String[] args) {
		Log logger = LogFactory.getLog(MonkeyRest.class);
		int port = 6666;
		if (args.length > 0) {
			port = Integer.valueOf(args[0]);
		}
		port(port);
		path("/dab", () -> {
			get("/open", (req, res) -> api.findAndOpen(true));

			get("/volume", (req, res) -> {
				byte vol = Byte.valueOf(req.queryParams("level"));
				return api.setVolume(vol);
			});

			get("/stereo", (req, res) -> api.setStereoMode(true));

			get("/program-list", (req, res) -> {
				int total = (int) api.getTotalPrograms();
				List<String> list = new ArrayList<>(total);
				for (int i=0; i<total; i++) {
					list.add("Found - ["+i+"]"+api.getProgramName(RadioMode.DAB, i, false));
				}
				return list;
			});

			get("/program-name", (req, res) -> {
				long dabProg = Long.valueOf(req.queryParams("prog"));
				return api.getProgramName(RadioMode.DAB, dabProg, false);
			});

			get("/play-program", (req, res) -> {
				long dabProg = Long.valueOf(req.queryParams("prog"));
				return api.playStream(RadioMode.DAB, dabProg);
			});
			
			get("/text", (req, res) -> {
				String text = api.getProgramText();
				logger.trace("Returning text: "+text);
				return text != null ? text : "";
			});

			get("/status", (res, req) -> api.getPlayStatus());

			get("/signal-strength", (res, req) -> api.getSignalStrength());

			get("/clear-db", (req, res) -> api.clearDatabase());

			get("/search", (req, res) -> api.dabAutoSearch((byte) 0, (byte) 40));
		});

	}

}
