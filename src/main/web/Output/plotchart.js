const express = require("express");
const app = express();

app.use(express.static("public"));

app.use(express.json());

app.get("/serverinfo", (req, res) => { res.json({ name: "First Node.js Server" }); });

app.listen(8000);
