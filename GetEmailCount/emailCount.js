

function testing(){
ContentService.createTextOutput("hello world!");
}
function getMails(spreadSheetFileName){
  var driveFiles = DriveApp.getFilesByName(spreadSheetFileName);
  var file = fileIte.next();

 if (file == null ) {
    throw "unable to find a file: " + spreadSheetFileName;
  }
  // Open in Spreadsheet app
  var spreadSheet = SpreadsheetApp.open(file);

  if ( spreadSheet == null) {
      throw spreadSheetFileName + " is not a spread sheet";
   }
  var outPutSheetName = "output";
  var outputDataSheet = spreadSheet.getSheetName(outPutSheetName);
  
  if ( outputDataSheet == null){ // if the sheet is not defined insert the sheet.
      outputDataSheet = spreadSheet.insertSheet(outPutSheetName);
   }
  
}
function run(){
  var outPut = getAllMailsFromTo("thilinama@wso2.com","vacation-group@wso2.com","2017/11/29" , '2017/11/9');
           Logger.log(outPut);

}



function getAllMailsFromTo(senderAddress, receiverAddress, before, after){
 
     var searchString = 'from:(' + senderAddress + ') to:(' +  receiverAddress+ ') after: '+after+' before: '+before;
     var threads = GmailApp.search(searchString)
     var result = [];
     if ( threads.length > 0){
           
       for (var i = 0; i < threads.length ; i++){
         var emailThread = threads[i]; 
         var subject = emailThread.getFirstMessageSubject();
         var messageBodies = emailThread.getMessages();
         var dataObject = { suject:subject, messeageBodies: messageBodies };
         result.push(dataObject);
       }

                   
     } else {
       Logger.log("Nothing")
     }
     return result;
}


function CountEmails (columnId, spreadSheetFileName, emailListSheetName, targetSheetName,summarySheetName, publicEmailList, after, before) {
  
   var fileIte = DriveApp.getFilesByName(spreadSheetFileName);
   var file = fileIte.next();
  
  if (file == null ) {
  
    throw "unable to find a file: " + spreadSheetFileName;

  }
  
  var spreadSheet = SpreadsheetApp.open(file);
  
   if ( spreadSheet == null) {
      throw spreadSheetFileName + " is not a spread sheet";

   }
  
   
   var emailSheet =  spreadSheet.getSheetByName(emailListSheetName); 
   
  if ( emailSheet == null ){
    throw "sheet containing the list of emails doesn't not exists: " +  emailListSheetName;
  }
  
  
   var targetSheet = spreadSheet.getSheetByName(targetSheetName);
   if ( targetSheet == null){ // if the sheet is not defined lets insert the sheet.
      targetSheet = spreadSheet.insertSheet(targetSheetName);
   }
  
   var summarySheet = spreadSheet.getSheetByName(summarySheetName);
   if ( summarySheet == null){ // if the sheet is not defined lets insert the sheet.
      summarySheet = spreadSheet.insertSheet(summarySheetName);
   }
  
   var sheetlastRow = emailSheet.getLastRow();
  
   var emailColumn  = emailSheet.getRange('A:A'); // 'A' column
  
   var c = emailColumn.getColumn();
  
   targetSheet.appendRow(["EMAIL ADDRESS", "NUMBER OF EMAIL SENT", "EMAIL TITLES"]);

  
   for ( var r = emailColumn.getRow(); r <= sheetlastRow ; r ++){
     
     var emailTitles = '';
     var currentEmail  = emailColumn.getCell(r, c).getValue();
     
     
     var numberOfEmailsSent = 0;
     
    
     
     if ( threads.length > 0){
           
            for (var th = 0; th < threads.length ; th++){
       
       var emailThread = threads[th]; 
       
       emailTitles = emailTitles +  emailThread.getFirstMessageSubject() + "\n";
       
       var messages =  emailThread.getMessages();
       
       for ( var m = 0; m < messages.length ; m ++) {
          var msg = messages[m];
            
         if (msg.getFrom().toLowerCase().indexOf(currentEmail.toLowerCase())  != -1){
           numberOfEmailsSent ++; 
         }
         
       }     
      
     }

     } else {
       emailTitles = "NO EMAILS FOUND DURING THE PERIOD";
     }
          
     targetSheet.appendRow([currentEmail, numberOfEmailsSent, emailTitles, searchString]);
     //Set summary sheet
     var currentRow = targetSheet.getLastRow();
     if (currentRow == 2) {
         summarySheet.getRange(1, (columnId + 1)).setValue(targetSheetName);
     }
     summarySheet.getRange(currentRow, (1)).setValue(currentEmail);
     summarySheet.getRange(currentRow, (columnId + 1)).setValue(numberOfEmailsSent);     
   }
  
}


