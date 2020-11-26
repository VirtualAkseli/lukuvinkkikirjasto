var ss = SpreadsheetApp.getActiveSpreadsheet(),
    sheet = ss.getSheetByName("Data");

/**
 * Scheduled main function
 */
function fetchData() {
  var projectUpdatedAt = fetchUpdatedAt();
  var sheetUpdatedAt = sheet.getRange(1, 2).getValue();
  if (sheetUpdatedAt !== projectUpdatedAt ) {
    Logger.log("updating...");
    sheet.getRange(1, 2).setValue(projectUpdatedAt);
    var response = UrlFetchApp.fetch(getURL());
    var json = JSON.parse(response.getContentText()),
        pattern = /\>([^<>]+)\((\s*\d+\s*\/\s*\d+\s*|\s*\d+\s*)\)\s*\</gm;
    write(matchAndMap(json, pattern));
  }
}

function fetchUpdatedAt() {
  var projectUrl = "https://api.github.com/projects/5974216";
  var options = {
    headers: {
      Accept: "application/vnd.github.inertia-preview+json",
      Authorization: "token " + PropertiesService.getScriptProperties().getProperty("TOKEN")
    }
  };
  var json = JSON.parse(UrlFetchApp.fetch(projectUrl, options));
  return json.updated_at;
}

/**
 * PhantomJsCloud.com is used for javascript rendering. ApiKey is saved in script properties.
 * @returns {string} encoded URL
 */
function getURL() {
  var requestJson = {
    // URL of the current sprint board
    url:"https://github.com/akirataguchi115/lukuvinkkikirjasto/projects/4",
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
  var date = rowArray[0][0].substr(0, 10);
  removePreviousEntries(date, sheet);
  for (var i = 0; i < rowArray.length; i++)
    sheet.appendRow(rowArray[i]);
}

function removePreviousEntries(date) {
  var lastRow = sheet.getLastRow();
  var savedData = sheet.getRange(2, 1, lastRow - 1).getValues(),
      count = 0;
  for (var i = savedData.length - 1; i > 0; i--) {
    if (savedData[i][0].toLocaleString("fi-FI").substr(0, 10) === date)
      count++;
    else break;
  }
  for (i = lastRow; i > lastRow - count; i--)
    sheet.deleteRow(i);
}