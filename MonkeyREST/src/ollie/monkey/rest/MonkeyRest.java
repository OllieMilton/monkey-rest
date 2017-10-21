package ollie.monkey.rest;

import static spark.Spark.*;

public class MonkeyRest {

	public static void main(String[] args) {
		get("/twat", (req, res) -> "<h1>twat</h1>");
	}
}
