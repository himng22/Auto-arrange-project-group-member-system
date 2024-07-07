const express = require("express");
const app = express();

app.use(express.static("public"));
const jsonFile = require("fs");

const data = jsonFile.readFileSync("./result1.jason", "utf8");

const teamArray = JSON.parse(data);


app.listen(8000);