/* $Id$
 * ======================================================================
 * Common structure and methods for jDispatcher SOAP clients
 * ====================================================================== */
using System;
using System.IO;
using System.Reflection;
using System.Text;

namespace AbstractWsClient
{
	/// <summary>
	/// Abstract desfinition of a client to the EBI tool Web Services.
	/// </summary>
	public abstract class AbstractWsClient
	{
		/// <value>
		/// Level of output produced. Used to implment --quiet and --verbose.
		/// </value>
		public int OutputLevel {
			get{return outputLevel;}
			set{
				if(value > -1) {
					outputLevel = value;
				}
			}
		}
		private int outputLevel = 1;
		/// <value>
		/// Level of debug output (default off).
		/// </value>
		public int DebugLevel {
			get{return debugLevel;}
			set{
				if(value > -1) {
					debugLevel = value;
				}
			}
		}
		private int debugLevel = 0;
		/// <value>
		///  Maximum interval between status checks when polling a submited job.
		/// </value>
		public int MaxCheckInterval {
			get{return maxCheckInterval;}
			set{
				if(value > 5000) {
					maxCheckInterval = value;
				}
			}
		}
		private int maxCheckInterval = 60000;
		/// <value>
		/// Parameter name to be used to get parameter details.
		/// </value>
		public string ParamName {
			get{return paramName;}
			set{paramName = value;}
		}
		private string paramName = null;
		/// <value>
		/// Output file name base
		/// </value>
		public string OutFile {
			get{return outFile;}
			set{outFile = value;}
		}
		private string outFile = null;
		/// <value>
		/// Output result format
		/// </value>
		public string OutFormat {
			get{return outFormat;}
			set{outFormat = value;}
		}
		private string outFormat = null;
		/// <value>
		/// User e-mail address for job submissions. 
		/// </value>
		public string Email {
			get{return email;}
			set{email = value;}
		}
		private string email = null;
		/// <value>
		/// Title for job.
		/// </value>
		public string JobTitle {
			get{return jobTitle;}
			set{jobTitle = value;}
		}
		private string jobTitle = null;
		/// <value>
		/// Job Id for getting status or results
		/// </value>
		public string JobId {
			get{return jobId;}
			set{jobId = value;}
		}
		private string jobId = null;
		/// <value>
		/// Submission mode: async or sync
		/// </value>
		public bool Async {
			get{return async;}
			set{async = value;}
		}
		private bool async = false;
		/// <value>
		/// Action to perform
		/// </value>
		public string Action {
			get{return action;}
			set{action = value;}
		}
		private string action = "unknown";
		/// <summary>
		/// Usage message for generic options.
		/// </summary>
		private const string genericOptsStr = @"[General]

      --params         :      : list tool parameters
      --paramDetail    : str  : information about a parameter
      --email          : str  : e-mail address
      --title          : str  : title for the job
      --async          :      : perform an asynchronous submission
      --jobid          : str  : job identifier
      --status         :      : get status of a job
      --resultTypes    :      : get list of result formats for a job
      --polljob        :      : get results for a job
      --outfile        : str  : name of the file results should be written to
                                (default is based on the jobid; ""-"" for STDOUT)
      --outformat      : str  : output format, see --resultTypes
      --help           :      : prints this help text
      --quiet          :      : decrease output
      --verbose        :      : increase output
      --debugLevel     : int  : set debug output level

Synchronous job:

  The results/errors are returned as soon as the job is finished.
  Usage: tool.exe --email <your@email> [options...] seqFile
  Returns: results as an attachment

Asynchronous job:

  Use this if you want to retrieve the results at a later time. The results
  are stored for up to 24 hours.
  Usage: tool.exe --async --email <your@email> [options...] seqFile
  Returns: jobid

  Use the jobid to query for the status of the job. If the job is finished,
  it also returns the results/errors.
  Usage: tool.exe --polljob --jobid <jobId> [--outfile string]
  Returns: string indicating the status of the job and if applicable, results
  as an attachment.
";
		
		/// <summary>
		/// Default constructor.
		/// </summary>
		public AbstractWsClient()
		{
			OutputLevel = 1; // Normal output
			DebugLevel = 0; // Debug output off.
			MaxCheckInterval = 60000; // 1 min between checks
			OutFile = null;
			OutFormat = null;
			Email = null;
			JobTitle = "My Sequence";
			JobId = null;
			Async = false;
			Action = "UNKNOWN";
		}

		/// <summary>
		/// Print the generic options usage message to STDOUT.
		/// </summary>
		protected void PrintGenericOptsUsage()  {
			Console.WriteLine(genericOptsStr);
		}
		
		/// <summary>
		/// Print a debug message at the specified level.
		/// </summary>
		/// <param name="methodName">
		/// A <see cref="System.String"/>
		/// </param>
		/// <param name="message">
		/// A <see cref="System.String"/>
		/// </param>
		/// <param name="level">
		/// A <see cref="System.Int32"/>
		/// </param>
		protected void PrintDebugMessage(string methodName, string message, int level) {
			if(level <= DebugLevel) {
				Console.Error.WriteLine("[{0}()] {1}", methodName, message);
			}
		}

		/// <summary>
		/// Construct a string of the fields of an object.
		/// </summary>
		/// <param name="obj">
		/// Object to get fields from. A <see cref="System.Object"/>
		/// </param>
		/// <returns>
		/// Name and the contents of fields as a string. A <see cref="System.String"/>
		/// </returns>
    	protected string ObjectFieldsToString(Object obj) {
      		PrintDebugMessage("ObjectFieldsToString", "Begin", 31);
      		StringBuilder strBuilder = new StringBuilder();
      		Type objType = obj.GetType();
      		PrintDebugMessage("ObjectFieldsToString", "objType: " + objType, 32);
      		foreach(FieldInfo info in objType.GetFields()) {
                PrintDebugMessage("ObjectFieldsToString", "info: " + info.Name, 32);
                if (info.FieldType.IsArray)
                {
					strBuilder.Append(info.Name + ":\n");
					foreach(Object subObj in (Object[])info.GetValue(obj)) {
						strBuilder.Append("\t" + subObj);
					}
				}
				else {
					strBuilder.Append(info.Name + ": " + info.GetValue(obj) + "\n");
				}
      		}
      		PrintDebugMessage("ObjectFieldsToString", "End", 31);
      		return strBuilder.ToString();
    	}

        /// <summary>
        /// Construct a string of the properties of an object.
        /// </summary>
        /// <param name="obj">
        /// Object to get properties from. A <see cref="System.Object"/>
        /// </param>
        /// <returns>
        /// Name and the contents of properties as a string. A <see cref="System.String"/>
        /// </returns>
        protected string ObjectPropertiesToString(Object obj)
        {
            PrintDebugMessage("ObjectPropertiesToString", "Begin", 31);
            StringBuilder strBuilder = new StringBuilder();
            Type objType = obj.GetType();
            PrintDebugMessage("ObjectPropertiesToString", "objType: " + objType, 32);
            foreach (PropertyInfo info in objType.GetProperties())
            {
                PrintDebugMessage("ObjectPropertiesToString", "info: " + info.Name, 32);
                if (info.PropertyType.IsArray)
                {
                    strBuilder.Append(info.Name + ":\n");
                    foreach (Object subObj in (Object[])info.GetValue(obj, null))
                    {
                        strBuilder.Append("\t" + subObj);
                    }
                }
                else
                {
                    strBuilder.Append(info.Name + ": " + info.GetValue(obj, null) + "\n");
                }
            }
            PrintDebugMessage("ObjectPropertiesToString", "End", 31);
            return strBuilder.ToString();
        }
		
		/// <summary>
		/// Print a progress message, at the specified output level.
		/// </summary>
		/// <param name="msg">Message to print.
		/// A <see cref="System.String"/>
		/// </param>
		/// <param name="level">Output level at which to print the message.
		/// A <see cref="System.Int32"/>
		/// </param>
		protected void PrintProgressMessage(String msg, int level) {
			if(OutputLevel >= level) Console.Error.WriteLine(msg);
		}

		/// <summary>
		/// Read data from a text file into a string.
		/// </summary>
		/// <param name="fileName">Name of the file to read data from.
		/// A <see cref="System.String"/>
		/// </param>
		/// <returns>Contents of the file as a string.
		/// A <see cref="System.String"/>
		/// </returns>
		protected string ReadTextFile(string fileName) {
			PrintDebugMessage("ReadTextFile", "Begin", 1);
      		PrintDebugMessage("ReadTextFile", "fileName: " + fileName, 2);
      		string retVal = "";
      		if(fileName == "-") { // Read from STDIN
        		retVal = Console.In.ReadToEnd();
      		}
      		else { // Read from file
        		StreamReader readStream = File.OpenText(fileName);
        		retVal = readStream.ReadToEnd();
        		readStream.Close();
      		}
      		PrintDebugMessage("ReadTextFile", "read " + retVal.Length + " characters", 1);
      		PrintDebugMessage("ReadTextFile", "End", 1);
      		return retVal;
		}
		
		// Read a file into a byte[].
		// TODO
		protected byte[] ReadFile(string fileName) {
			PrintDebugMessage("ReadFile", "Begin", 1);
			PrintDebugMessage("ReadFile", "fileName: " + fileName, 1);
			byte[] retVal = null;
			
			PrintDebugMessage("ReadFile", "read " + retVal.Length + " bytes", 1);
			PrintDebugMessage("ReadFile", "End", 1);
			return retVal;
		}
		
		// Load the data to be submitted to the tool.
		// TODO
		protected byte[] LoadData(string fileOptionStr) {
			byte[] retVal = null;
			return retVal;
		}
		
		// Write a byte[] to a file.
		protected void WriteFile(string fileName, byte[] content) {
			PrintDebugMessage("WriteFile", "Begin", 1);
			PrintDebugMessage("WriteFile", "fileName: " + fileName, 1);
			PrintDebugMessage("WriteFile", "content: " + content.Length + " bytes", 1);
			PrintDebugMessage("WriteFile", "End", 1);
		}
		
		/// <summary>
		/// Get the service connection. Has to be called before attempting to use any of the service operations.
		/// </summary>
		protected abstract void ServiceProxyConnect();
		
		/// <summary>
		/// Get list of input parameter names from sevice.
		/// </summary>
		/// <returns>An array of parameter names.
		/// A <see cref="System.String"/>
		/// </returns>
		public abstract string[] GetParams();

		/// <summary>
		/// Print a list of input parameter names.
		/// </summary>
		protected void PrintParams() {
			PrintDebugMessage("PrintParams", "Begin", 1);
			string[] paramNameList = GetParams();
			foreach(string paramName in paramNameList) {
				Console.WriteLine(paramName);
			}
			PrintDebugMessage("PrintParams", "End", 1);
		}
		
		// Get details for a prameter.
		
		// 
		
		/// <summary>
		/// Print a detailed description of a specified input parameter.
		/// </summary>
		/// <param name="paramName">Name of the parameter to print the detailed description of.
		/// A <see cref="System.String"/>
		/// </param>
		protected abstract void PrintParamDetail(string paramName);
		
		/// <summary>
		/// Submit a job using the current client state.
		/// </summary>
		public abstract void SubmitJob();
		
		/// <summary>
		/// Get the status of a submitted job.
		/// </summary>
		/// <param name="jobId">Job identifier.
		/// A <see cref="System.String"/>
		/// </param>
		/// <returns>A string describing the status of the job.
		/// A <see cref="System.String"/>
		/// </returns>
		public abstract string GetStatus(string jobId);
		
		/// <summary>
		/// Print the status of the current job.
		/// </summary>
    	public void PrintStatus() {
      		PrintDebugMessage("PrintStatus", "Begin", 1);
      		string status = GetStatus(JobId);
      		Console.WriteLine(status);
      		PrintDebugMessage("PrintStatus", "End", 1);
    	}
		
		/// <summary>
		/// Wait for a job to finish.
		/// </summary>
		/// <param name="jobId">Job identifier of the job to wait for.
		/// A <see cref="System.String"/>
		/// </param>
    	public void ClientPoll(string jobId) {
      		PrintDebugMessage("ClientPoll", "Begin", 1);
      		PrintDebugMessage("ClientPoll", "jobId: " + jobId, 2);
      		string status = "PENDING";
      		// Check status and wait if not finished
      		while(status == "RUNNING" || status == "PENDING") {
        		status = GetStatus(JobId);
        		if(OutputLevel > 0) {
          			Console.WriteLine(status);
        		}
				// TODO: use progressive delay.
        		if(status == "RUNNING" || status == "PENDING") {
          			// Wait before polling again.
          			System.Threading.Thread.Sleep(15000);
        		}
      		}
      		PrintDebugMessage("ClientPoll", "End", 1);
    	}
		
		/// <summary>
		/// Print a list of the available result types for the current job.
		/// </summary>
		public abstract void PrintResultTypes();
		
		/// <summary>
		/// Get results for a job, of the specified type.
		/// </summary>
		/// <param name="jobId">Job identifier.
		/// A <see cref="System.String"/>
		/// </param>
		/// <param name="outformat">Required result format. If null all available formats are retrived.
		/// A <see cref="System.String"/>
		/// </param>
		/// <param name="outFileBase">Base of generated filenames, used to store results. If null the filename is based on the job identifier.
		/// A <see cref="System.String"/>
		/// </param>
		public abstract void GetResults(string jobId, string outformat, string outFileBase);
		
  		/// <summary>
  		/// Get results for a job using the current format and output file.
  		/// </summary>
		/// <param name="jobId">Job identifier.
		/// A <see cref="System.String"/>
		/// </param>
    	public void GetResults(string jobId) {
     		PrintDebugMessage("PollJob", "Begin", 1);
      		GetResults(jobId, OutFormat, OutFile);
      		PrintDebugMessage("PollJob", "End", 1);
    	}
		
  		/// <summary>
  		/// Get results for the current job
  		/// </summary>
    	public void GetResults() {
     		PrintDebugMessage("PollJob", "Begin", 1);
      		GetResults(JobId, OutFormat, OutFile);
      		PrintDebugMessage("PollJob", "End", 1);
    	}
		
	}
}
