const express = require("express");
const app = express();
// Middleware to parse JSON bodies
app.use(express.json());
const port = 3000;

let users = [];

app.get("/", (req, res) => {
  res.send("Hello users:" + users);
});


///NEW POST FUNCTION
app.post("/", (req, res) => {
  if (!req.body || !req.body.name) {
    res.status(400).send("Missing name in req.body");
    return;
  }
  users.push(req.body.name);
  res.send("Created user " + req.body.name);
});

app.listen(port, () => {
  console.log(`Users app listening at http://localhost:${port}`);
});
