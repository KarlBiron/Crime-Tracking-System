/*
 *  Licensed Materials - Property of IBM
 *  5725-I43 (C) Copyright IBM Corp. 2011, 2016. All Rights Reserved.
 *  US Government Users Restricted Rights - Use, duplication or
 *  disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

var getInvestigatorUsernameStatement = "SELECT fullname FROM investigators_tbl  " +
	"WHERE username = ? and password = ? " + "ORDER BY fullname DESC " + "LIMIT 1;";

function validateInvestigator(username, password){
	return MFP.Server.invokeSQLStatement({
	preparedStatement : getInvestigatorUsernameStatement,
	parameters : [username, password]
	});

}


var getListAllCrimesStatement = "SELECT * FROM crimes_tbl  " +
" " + "ORDER BY date_time DESC ";

function getListAllCrimes(){
	
	return MFP.Server.invokeSQLStatement({
			preparedStatement : getListAllCrimesStatement,
			parameters : []
			});

}

var addCrimeStatement =  "INSERT INTO crimes_tbl ( date_time, location, victims, suspects, evidences)" + 
				" VALUES (?, ?, ?, ?, ?) ;";

function addCrime(date_time,  victims, location, suspects, evidences){
	return MFP.Server.invokeSQLStatement({
			preparedStatement: addCrimeStatement,
			parameters : [date_time,location,  victims,  suspects,  evidences ]
	});
}



var searchBySuspectStatement = "SELECT * FROM crimes_tbl  " +
" WHERE suspects LIKE ? ESCAPE '!'" +
" " + "ORDER BY date_time DESC ";

function searchBySuspect(suspect){
	
	return MFP.Server.invokeSQLStatement({
			preparedStatement : searchBySuspectStatement,
			parameters : [new String("%" + suspect + "%")]
			});

}


var findSerialCrimetStatement = "SELECT *, MATCH (location, victims, suspects, evidences) " +
		" AGAINST (?) AS score   " +
		"   FROM crimes_tbl WHERE MATCH (location, victims, suspects, evidences) AGAINST (?); " ;

function findSerialCrime(param){
	
	return MFP.Server.invokeSQLStatement({
			preparedStatement : findSerialCrimetStatement,
			parameters : [param, param]
			});

}

var getLastCrimeStatement = "SELECT * FROM crimes_tbl  " +
" WHERE reference =   LAST_INSERT_ID()" +
"" + "ORDER BY date_time DESC ";

function getLastCrime(){
	
	return MFP.Server.invokeSQLStatement({
			preparedStatement : getLastCrimeStatement,
			parameters : []
			});

}



/************************************************************************
 * Implementation code for procedure - 'procedure1'
 *
 *
 * @return - invocationResult
 */
var getAccountsTransactionsStatement = "SELECT transactionId, fromAccount, toAccount, transactionDate, transactionAmount, transactionType " +
"FROM accounttransactions " + "WHERE accounttransactions.fromAccount = ? OR accounttransactions.toAccount = ? " + "ORDER BY transactionDate DESC " + "LIMIT 20;";

function getAccountTransactions1(accountId){
	return MFP.Server.invokeSQLStatement({
	preparedStatement : getAccountsTransactionsStatement,
	parameters : [accountId, accountId]
	});
}

function getAccountTransactions2(accountId){
return MFP.Server.invokeSQLStoredProcedure({
procedure : "getAccountTransactions",
parameters : [accountId]
});
}


/************************************************************************
 * Implementation code for procedure - 'procedure2'
 *
 *
 * @return - invocationResult
 */
 
function procedure2(param) {
	return MFP.Server.invokeSQLStoredProcedure({
		procedure : "storedProcedure2",
		parameters : [param]
	});
}

/************************************************************************
 * Implementation code for procedure - 'unprotected'
 *
 *
 * @return - invocationResult
 */
function unprotected(param) {
	return {result : "Hello from unprotected resource"};
}


