/**
 * Scheduled main function
 */
function fetchData() {
  var response = UrlFetchApp.fetch(getURL());
  var json = JSON.parse(response.getContentText());
  var pattern = /\>([^<>]+)\((\s*\d+\s*\/\s*\d+\s*|\s*\d+\s*)\)\s*\</gm;
  write(matchAndMap(json, pattern));
}

/**
 * PhantomJsCloud.com is used for javascript rendering. ApiKey is saved in script properties.
 * @returns {string} encoded URL
 */
function getURL() {
  var requestJson = {
    // URL of the current sprint board
    url:"https://github.com/akirataguchi115/lukuvinkkikirjasto/projects/3",
    renderType:"plainText",
    outputAsJson:true
  };
  var key = PropertiesService.getScriptProperties().getProperty("API_KEY");
  return "https://PhantomJsCloud.com/api/browser/v2/" + key + "/?request=" +
      encodeURIComponent(JSON.stringify(requestJson));
}

function matchAndMap(json, pattern) {
  var workingTimes = [],
      matchedTitles = [],
      match;
  var currentTime = new Date().toLocaleString("fi-FI");
  while (match = pattern.exec(json.pageResponses[0].frameData.content)) {
    if (matchedTitles.indexOf(match[1]) === -1) {
      matchedTitles.push(match[1]);
      workingTimes.push([currentTime, match[1], match[2]]);
    }
  }
  return workingTimes;
}

function write(rowArray) {
  var ss = SpreadsheetApp.getActiveSpreadsheet();
  var sheet = ss.getSheetByName("Data");
  for (var i = 0; i < rowArray.length; i++) {
    sheet.appendRow(rowArray[i]);
  }
}