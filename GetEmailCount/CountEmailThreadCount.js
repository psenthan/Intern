function Count(){ 
  
  CountEmails("Have to specific parmeters here!!!!!! according to the function you see below");
  

  }


function GetSpreadSheet(spreadSheetFileName){
  
   var fileIte = DriveApp.getFilesByName(spreadSheetFileName);
   var file = fileIte.next();
  
   if (file == null ) {
       throw "unable to find a file: " + spreadSheetFileName;
   }
  
    var spreadSheet = SpreadsheetApp.open(file);
  
    if ( spreadSheet == null) {
      throw spreadSheetFileName + " is not a spread sheet";
   }
   
  return spreadSheet;
  
 }



function CountEmails (columnId, spreadSheetFileName, emailListSheetName, targetSheetName,summarySheetName, publicEmailList, after, before) {
  
  
  var spreadSheet = GetSpreadSheet(spreadSheetFileName);
  
   
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
  
   targetSheet.appendRow(["EMAIL ADDRESS", "NUMBER OF EMAIL SENT", "THREADS COUNT","EMAIL TITLES"]);

  
   for ( var r = emailColumn.getRow(); r <= sheetlastRow ; r ++){
     
     var emailTitles = '';
     var currentEmail  = emailColumn.getCell(r, c).getValue();
     
     
     var numberOfEmailsSent = 0;
     var threadsCount = 0;
     
     var searchString = 'from:' + currentEmail + ' to:(' + publicEmailList + ') after:' + after + ' before:' + before;
     var threads = GmailApp.search(searchString);
     
     if ( threads.length > 0){
       threadsCount = threads.length;
       
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
          
     targetSheet.appendRow([currentEmail, numberOfEmailsSent,threadsCount,emailTitles]);
     //Set summary sheet
     var currentRow = targetSheet.getLastRow();
     if (currentRow == 2) {
         summarySheet.getRange(1, (columnId + 1)).setValue(targetSheetName);
         summarySheet.getRange(1, (columnId + 2)).setValue(targetSheetName + "+Threads");
     }
     summarySheet.getRange(currentRow, (1)).setValue(currentEmail);
     summarySheet.getRange(currentRow, (columnId + 1)).setValue(numberOfEmailsSent);
     summarySheet.getRange(currentRow, (columnId + 2)).setValue(threadsCount);     
   }
  
}


