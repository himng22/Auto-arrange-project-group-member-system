const http = require("http");       // import the http library of node
const fs = require("fs");           // import the file system library
const url = require("url")

const port = 8000       // the port number we are going to listen for

const server = http.createServer(function(req,res){     // create a server

    // parsing the request from public website
    let parserdURL = url.parse(req.url, true);
    let path = parserdURL.pathname;
    // trimming off the '/' at the front and at the end by regex
    path = path.replace(/^\/+|\/+$/g, "");      // regex stuff

    //path = "comp3111_ATU.json";     // example

    // preparing response
    // writeHead status 200 means OK
    res.setHeader("Access-Control-Allow-Origin", "*");      // this means we allow request from any browser, any domain

    // read the required json
    try {
        console.log(path);
        data = fs.readFileSync("../../resources/atuOutput/" + path, "utf8");       // read the required file
        res.writeHead(200, {"Content-type" : "application/json"});     
        res.write(data);                                            // write the data to response
    } catch (readfileErr) {                                         // handle file io error 
        res.writeHead(404); 
        res.write("Error: required file not found");
    }

    res.end();      // finish the response
})


// Full Example : "http://localhost:8000/comp3111_ATU.json"
// "http://localhost:8000"
server.listen(port, function(){
    console.log("Server listening on port ", port)
})