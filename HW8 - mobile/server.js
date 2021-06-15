'use strict';

// [START gae_node_request_example]
const express = require('express');
const url = require('url');
const app = express();




// [END gae_node_request_example]
// [START add_display_form]
app.get('/submit', (req, res) => {
    res.sendFile(path.join(__dirname, '/static/search.html'));
  });
// Start the server
const PORT = process.env.PORT || 8080;
app.listen(PORT, () => {
  console.log(`App listening on port ${PORT}`);
  console.log('Press Ctrl+C to quit.');
});

module.exports = app;