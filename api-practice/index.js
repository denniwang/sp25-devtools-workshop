const express = require("express");
const app = express();
const port = 3000;

let users = [];

app.get("/", (req, res) => {
  res.send("Hello users:" + users)
});

app.listen(port, () => {
  console.log(`Users app listening at http://localhost:${port}`);
});
