package restclient;

// Copyright 2012-2018 EMBL - European Bioinformatics Institute
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//
// Java Client Automatically generated with:
// https://github.com/ebi-wp/webservice-clients-generator
//
// PSI-Blast (REST) web service Java client.

import com.google.common.io.Resources;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.representation.Form;
import org.apache.commons.cli.*;
import restclient.stubs.WsParameter;
import restclient.stubs.WsParameters;
import restclient.stubs.WsResultType;
import restclient.stubs.WsResultTypes;

import javax.xml.bind.JAXBException;
import javax.xml.rpc.ServiceException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;


public class RestClientpsiblast {

    static {
        System.setProperty("org.slf4j.simpleLogger.defaultLogLevel", "ERROR");
    }

    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(RestClientpsiblast.class);

    private String revision = "2018";
    private Client client;
    private final String toolId;
    private final String toolName;
    private final String toolDescription;
    private String baseUrl = "";
    private final String defaultHost = "www.ebi.ac.uk";
    private final String defaultProtocol = "https";
    private final String defaultUrlPath = "/Tools/services/rest/";
    public int outputLevel = 1;
    public int debugLevel = 0;
    public int pollFreq = 3;

    private final Options allOptions = new Options();

    private String requiredParameters;
    private String requiredParametersMessage;
    private String optionalParameters;
    private String optionalParametersMessage;

    private final String generalInfo = "EMBL-EBI PSI-Blast Java Client:\n"
                                   + "\n"
                                   + "Sequence similarity search with PSI-Blast.\n"
                                   + "\n"
                                   + "[Required (for job submission)]\n"
                                   + "  --email               E-mail address.\n"
                                   + "  --sequence            The query sequence can be entered directly into this form.\n"
                                   + "                        The sequence can be in GCG, FASTA, PIR, NBRF, PHYLIP or\n"
                                   + "                        UniProtKB/Swiss-Prot format. A partially formatted sequence\n"
                                   + "                        is not accepted. Adding a return to the end of the sequence\n"
                                   + "                        may help certain applications understand the input. Note\n"
                                   + "                        that directly using data from word processors may yield\n"
                                   + "                        unpredictable results as hidden/control characters may be\n"
                                   + "                        present.\n"

                                   + "  --database            The databases to run the sequence similarity search against.\n"
                                   + "                        Multiple databases can be used at the same time.\n"


                                   + "\n[Optional]\n"
                                   + "  --matrix              The comparison matrix to be used to score alignments when\n"
                                   + "                        searching the database.\n"

                                   + "  --gapopen             Penalty taken away from the score when a gap is created in\n"
                                   + "                        sequence. Increasing the gap openning penalty will decrease\n"
                                   + "                        the number of gaps in the final alignment.\n"

                                   + "  --gapext              Penalty taken away from the score for each base or residue\n"
                                   + "                        in the gap. Increasing the gap extension penalty favors\n"
                                   + "                        short gaps in the final alignment, conversly decreasing the\n"
                                   + "                        gap extension penalty favors long gaps in the final\n"
                                   + "                        alignment.\n"

                                   + "  --expthr              Limits the number of scores and alignments reported based on\n"
                                   + "                        the expectation value. This is the maximum number of times\n"
                                   + "                        the match is expected to occur by chance.\n"

                                   + "  --psithr              Expectation value threshold for automatic selection of\n"
                                   + "                        matched sequences for inclusion in PSSM at each iteratio.\n"

                                   + "  --scores              Maximum number of match score summaries reported in the\n"
                                   + "                        result output.\n"

                                   + "  --alignments          Maximum number of match alignments reported in the result\n"
                                   + "                        output.\n"

                                   + "  --alignView           Formating for the alignments.\n"

                                   + "  --dropoff             The amount a score can drop before extension of word hits is\n"
                                   + "                        halted.\n"

                                   + "  --finaldropoff        Dropoff value for final gapped alignment.\n"

                                   + "  --filter              Filter regions of low sequence complexity. This can avoid\n"
                                   + "                        issues with low complexity sequences where matches are found\n"
                                   + "                        due to composition rather than meaningful sequence\n"
                                   + "                        similarity. However in some cases filtering also masks\n"
                                   + "                        regions of interest and so should be used with cautio.\n"

                                   + "  --seqrange            Specify a range or section of the input sequence to use in\n"
                                   + "                        the search. Example: Specifying '34-89' in an input sequence\n"
                                   + "                        of total length 100, will tell BLAST to only use residues 34\n"
                                   + "                        to 89, inclusive.\n"

                                   + "  --previousjobid       The job identifier for the previous PSI-BLAST iteratio.\n"

                                   + "  --selectedHits        List of identifiers of the hits from the previous iteration\n"
                                   + "                        to use to construct the search PSSM for this iteratio.\n"

                                   + "  --cpfile              Checkpoint file from the previous iteration. Must be in\n"
                                   + "                        ASN.1 Binary Format.\n"

                                   + "  --umode               Usage mode for PHI-BLAST functionality.\n"

                                   + "  --patfile             Pattern file for PHI-BLAST functionality. This file needs to\n"
                                   + "                        be in the style of a prosite entry file, with at least an ID\n"
                                   + "                        line, PA line and optional HI line.\n"


                                   + "\n[General]\n"
                                   + "  -h, --help            Show this help message and exit.\n"
                                   + "  --async               Forces to make an asynchronous query.\n"
                                   + "  --title               Title for job.\n"
                                   + "  --status              Get job status.\n"
                                   + "  --resultTypes         Get available result types for job.\n"
                                   + "  --polljob             Poll for the status of a job.\n"
                                   + "  --pollFreq            Poll frequency in seconds (default 3s).\n"
                                   + "  --jobid               JobId that was returned when an asynchronous job was submitted.\n"
                                   + "  --outfile             File name for results (default is JobId; for STDOUT).\n"
                                   + "  --outformat           Result format(s) to retrieve. It accepts comma-separated values.\n"
                                   + "  --params              List input parameters.\n"
                                   + "  --paramDetail         Display details for input parameter.\n"
                                   + "  --verbose             Increase output.\n"
                                   + "  --quiet               Decrease output.\n"
                                   + "  --baseUrl             Base URL. Defaults to:\n"
                                   + "                        https://www.ebi.ac.uk/Tools/services/rest/psiblast\n"
                                   + "\n"
                                   + "Synchronous job:\n"
                                   + "  The results/errors are returned as soon as the job is finished.\n"
                                   + "  Usage: java -jar psiblast.jar --email <your@email.com> [options...] <SeqFile|SeqID(s)>\n"
                                   + "  Returns: results as an attachment\n"
                                   + "\n"
                                   + "Asynchronous job:\n"
                                   + "  Use this if you want to retrieve the results at a later time. The results\n"
                                   + "  are stored for up to 24 hours.\n"
                                   + "  Usage: java -jar psiblast.jar --async --email <your@email.com> [options...] <SeqFile|SeqID(s)>\n"
                                   + "  Returns: jobid\n"
                                   + "\n"
                                   + "Check status of Asynchronous job:\n"
                                   + "   Usage: java -jar psiblast.jar --status --jobid <jobId>\n"
                                   + "\n"
                                   + "Retrieve job data:\n"
                                   + "  Use the jobid to query for the status of the job. If the job is finished,\n"
                                   + "  it also returns the results/errors.\n"
                                   + "  Usage: java -jar psiblast.jar --polljob --jobid <jobId> [--outfile string]\n"
                                   + "  Returns: string indicating the status of the job and if applicable, results\n"
                                   + "  as an attachment.\n"
                                   + "\n"
                                   + "Further information:\n"
                                   + "  https://www.ebi.ac.uk/Tools/webservices and\n"
                                   + "    https://github.com/ebi-wp/webservice-clients\n"
                                   + "\n"
                                   + "Support/Feedback:\n"
                                   + "  https://www.ebi.ac.uk/support/";

    /**
     * Entry point to the application
     *
     * @param args
     * @throws IOException
     * @throws InterruptedException
     * @throws ServiceException
     */
    public static void main(String[] args) throws IOException, InterruptedException, ServiceException, JAXBException {

        RestClientpsiblast client = new RestClientpsiblast();
        client.run(args);
    }

    /**
     * Load tool specific information in the constructor
     */
    private RestClientpsiblast() throws IOException {

        toolId = "psiblast";
        toolName = "PSI-Blast";
        toolDescription = "Sequence similarity search with PSI-Blast.";
        printDebugMessage("RestClient", String.format("Tool id: %s", toolId), 1);
        printDebugMessage("RestClient", String.format("Tool name: %s", toolName), 1);
        setUserAgent();
    }

    private String getServiceEndpoint() {
        return new StringBuilder(defaultProtocol)
                .append("://")
                .append(defaultHost)
                .append(defaultUrlPath).toString();
    }

    /**
     * Set user agent for http request with SVN revision, EBI phrase, class name and OS name
     */
    private void setUserAgent() {
        String currentUserAgent = System.getProperty("http.agent");

        String clientVersion = revision;
        String clientUserAgent = "EBI-Sample-Client/" + clientVersion + " (" + this.getClass().getName() + "; " + System.getProperty("os.name") + ")";
        if (currentUserAgent != null) {
            System.setProperty("http.agent", clientUserAgent + " " + currentUserAgent);
        } else {
            System.setProperty("http.agent", clientUserAgent);
        }

        printDebugMessage("setUserAgent", String.format("http.agent: %s", System.getProperty("http.agent").toString()), 1);
    }

    private int getOutputLevel() {
        return outputLevel;
    }

    private void setOutputLevel(int level) {
        this.outputLevel = level;
    }

    private int getDebugLevel() {
        return debugLevel;
    }

    private void setDebugLevel(int level) {
        this.debugLevel = level;
    }

    private int getPollFreq() {
        return pollFreq;
    }

    private void setPollFreq(int freq) {
        this.pollFreq = freq;
    }

    /**
     * Return Client form Jersey library
     *
     * @return
     */
    private Client getClient() {
        if (client == null) {
            client = Client.create();
        }
        return client;
    }

    /**
     * Print usage message
     */
    public void printUsage() {
        StringBuilder sb = new StringBuilder("");
        sb.append(generalInfo);
        System.out.println(sb.toString());
        printDebugMessage("printUsage", String.format("%s", sb.toString()), 11);
    }

    /**
     * Print debug message
     */
    public void printDebugMessage(String functionName, String message, int level) {
        debugLevel = getDebugLevel();
        if (level <= debugLevel)
            System.out.println("[" + functionName + "] " + message);
    }


    /**
     * Print job status
     */
    public void printGetStatus(String jobid) throws IOException, ServiceException {

        printDebugMessage("printGetStatus", "Getting status for job " + jobid, 11);
        outputLevel = getOutputLevel();
        if (outputLevel > 0)
            System.out.println("Getting status for job " + jobid);
        String status = checkStatus(jobid);
        System.out.println(status);
        if (outputLevel > 0 && status.equals("FINISHED")) {
             System.out.println("To get results: java -jar psiblast.jar --polljob --jobid " + jobid);
        }
    }

    /**
     * Set options common to all tools
     */
    private void addGeneralOptions() {
        allOptions.addOption("matrix", "", true, "The comparison matrix to be used to score alignments when searching"
                   + "the database");
        allOptions.addOption("gapopen", "", true, "Penalty taken away from the score when a gap is created in sequence."
                   + "Increasing the gap openning penalty will decrease the number of gaps"
                   + "in the final alignment.");
        allOptions.addOption("gapext", "", true, "Penalty taken away from the score for each base or residue in the gap."
                   + "Increasing the gap extension penalty favors short gaps in the final"
                   + "alignment, conversly decreasing the gap extension penalty favors long"
                   + "gaps in the final alignment.");
        allOptions.addOption("expthr", "", true, "Limits the number of scores and alignments reported based on the"
                   + "expectation value. This is the maximum number of times the match is"
                   + "expected to occur by chance.");
        allOptions.addOption("psithr", "", true, "Expectation value threshold for automatic selection of matched"
                   + "sequences for inclusion in PSSM at each iteration.");
        allOptions.addOption("scores", "", true, "Maximum number of match score summaries reported in the result output.");
        allOptions.addOption("alignments", "", true, "Maximum number of match alignments reported in the result output.");
        allOptions.addOption("alignView", "", true, "Formating for the alignments");
        allOptions.addOption("dropoff", "", true, "The amount a score can drop before extension of word hits is halted");
        allOptions.addOption("finaldropoff", "", true, "Dropoff value for final gapped alignment");
        allOptions.addOption("filter", "", true, "Filter regions of low sequence complexity. This can avoid issues with"
                   + "low complexity sequences where matches are found due to composition"
                   + "rather than meaningful sequence similarity. However in some cases"
                   + "filtering also masks regions of interest and so should be used with"
                   + "caution.");
        allOptions.addOption("seqrange", "", true, "Specify a range or section of the input sequence to use in the search."
                   + "Example: Specifying 34-89 in an input sequence of total length 100,"
                   + "will tell BLAST to only use residues 34 to 89, inclusive.");
        allOptions.addOption("sequence", "", true, "The query sequence can be entered directly into this form. The"
                   + "sequence can be in GCG, FASTA, PIR, NBRF, PHYLIP or UniProtKB/Swiss-"
                   + "Prot format. A partially formatted sequence is not accepted. Adding a"
                   + "return to the end of the sequence may help certain applications"
                   + "understand the input. Note that directly using data from word"
                   + "processors may yield unpredictable results as hidden/control"
                   + "characters may be present.");
        allOptions.addOption("database", "", true, "The databases to run the sequence similarity search against. Multiple"
                   + "databases can be used at the same time");
        allOptions.addOption("previousjobid", "", true, "The job identifier for the previous PSI-BLAST iteration.");
        allOptions.addOption("selectedHits", "", true, "List of identifiers of the hits from the previous iteration to use to"
                   + "construct the search PSSM for this iteration.");
        allOptions.addOption("cpfile", "", true, "Checkpoint file from the previous iteration. Must be in ASN.1 Binary"
                   + "Format.");
        allOptions.addOption("umode", "", true, "Usage mode for PHI-BLAST functionality");
        allOptions.addOption("patfile", "", true, "Pattern file for PHI-BLAST functionality. This file needs to be in the"
                   + "style of a prosite entry file, with at least an ID line, PA line and"
                   + "optional HI line.");


        allOptions.addOption("email", "", true, "E-mail address.");
        allOptions.addOption("h", "help", false, "Show this help message and exit.");
        allOptions.addOption("params", "", false, "Print parameters.");
        allOptions.addOption("paramDetail", "", true, "Details about selected parameter.");
        allOptions.addOption("title", "", true, "Title for the job.");
        allOptions.addOption("jobid", "", true, "Job identifier.");
        allOptions.addOption("async", "", false, "Run job as an asynchronous task, (default: synchronous).");
        allOptions.addOption("status", "", false, "Status of a job.");
        allOptions.addOption("resultTypes", "", false, "Get list of output formats.");
        allOptions.addOption("polljob", "", false, "Get results for the job.");
        allOptions.addOption("outformat", "", true, "Output format.");
        allOptions.addOption("quiet", "", false, "Decrease output level.");
        allOptions.addOption("verbose", "", false, "Increase output level.");
        allOptions.addOption("debugLevel", "", true, "Debugging level.");
        allOptions.addOption("baseUrl", "", true, "Custom host.");
    }


    /**
     * Get tool's parameters from server
     *
     * @return
     */
    public WsParameters getParams() {
        ClientResponse response = getResponse(baseUrl, "/parameters", RequestType.GET, null);

        if (ClientUtilspsiblast.isResponseCorrect(response, debugLevel)) {
            return response.getEntity(WsParameters.class);
        }
        return null;
    }


    /**
     * Get from server detailed information about queried parameter
     *
     * @param paramDetail
     * @return
     */
    public WsParameter getParam(String paramDetail) {
        ClientResponse response = getResponse(baseUrl, "/parameterdetails/" + paramDetail, RequestType.GET, null);

        if (ClientUtilspsiblast.isResponseCorrect(response, debugLevel)) {
            return response.getEntity(WsParameter.class);
        }
        return null;
    }


    /**
     * Status of a given job.
     * The values for the status are:
     * RUNNING: the job is currently being processed.
     * FINISHED: job has finished, and the results can then be retrieved.
     * ERROR: an error occurred attempting to get the job status.
     * FAILURE: the job failed.
     * NOT_FOUND: the job cannot be found.
     *
     * @param jobid
     * @return
     * @throws IOException
     * @throws ServiceException
     */
    public String checkStatus(String jobid) throws IOException, ServiceException {
        outputLevel = getOutputLevel();
        ClientResponse response = getResponse(baseUrl, "/status/" + jobid, RequestType.GET, null);
        if (ClientUtilspsiblast.isResponseCorrect(response, debugLevel)) {
            final String status = response.getEntity(String.class);
            return status;
        } else {
            System.out.println(response.getEntity(String.class));
        }
        return null;
    }


    /**
     * Get result types for a job from server
     *
     * @param jobId
     * @return
     */
    public WsResultTypes getResultTypesForJobId(String jobId) {
        ClientResponse response = getResponse(baseUrl, "/resulttypes/" + jobId, RequestType.GET, null);
        if (ClientUtilspsiblast.isResponseCorrect(response, debugLevel)) {
            return response.getEntity(WsResultTypes.class);
        }
        return null;
    }


    /**
     * Entry point to the application
     *
     * @param args
     */
    public void run(String[] args) {

        addGeneralOptions();
        CommandLineParser cliParser = new GnuParser();

        try {
            CommandLine cli = cliParser.parse(allOptions, args);

            List<String> unusedargs = cli.getArgList();

            // Print just basic info
            if (args.length == 0) {
                printUsage();
                return;
            }

            if (cli.hasOption("help") || cli.hasOption("h")) {
                printUsage();
                return;
            }

            if (cli.hasOption("verbose")) {
                setOutputLevel(getOutputLevel() + 1);
            }
            if (cli.hasOption("quiet")) {
                setOutputLevel(getOutputLevel() - 1);
            }
            int outputLevel = getOutputLevel();

            if (cli.hasOption("debugLevel")) {
                setDebugLevel(Integer.valueOf(cli.getOptionValue("debugLevel")));
            }
            int debugLevel = getDebugLevel();

            if (cli.hasOption("pollFreq")) {
                setPollFreq(Integer.valueOf(cli.getOptionValue("pollFreq")));
            }
            int pollFreq = getPollFreq();

            String sequence = null;
            String asequence = null;
            String bsequence = null;
            if (cli.hasOption("sequence")) {
                sequence = cli.getOptionValue("sequence");
            } else if (unusedargs.size() > 0) {
                sequence = unusedargs.get(0);
            } else if (cli.hasOption("asequence") && cli.hasOption("bsequence")) {
                asequence = cli.getOptionValue("asequence");
                bsequence = cli.getOptionValue("bsequence");
            }

            if (cli.hasOption("baseUrl")) {
                baseUrl = cli.getOptionValue("baseUrl");
            } else {
                baseUrl = getServiceEndpoint() + toolId;
            }

            if (cli.hasOption("params")) {
                WsParameters params = getParams();

                if (params != null) {
                    for (String param : params.getIds()) {
                        System.out.println(param);
                    }
                    printDebugMessage("run", ("Tool specific parameters: " + params.getIds().toString()), 11);

                    StringBuilder sb = new StringBuilder();
                    sb.append("\n\nTo learn more about selected parameter run:\n")
                            .append("java -jar ").append("psiblast.jar")
                            .append(" --paramDetails <parameter-name>")
                            .append(params.getIds().size() > 0 ? "\n\nFor example:\njava -jar psiblast.jar --paramDetails " + params.getIds().get(0) : "");

                    printDebugMessage("run", sb.toString(), 11);
                } else {
                    printDebugMessage("run", "Returned WsParameters object is null", 41);
                }

            }
            // Details of selected parameter
            else if (cli.hasOption("paramDetail")) {

                WsParameter parameter = getParam(cli.getOptionValue("paramDetail"));
                if (parameter != null) {

                    printDebugMessage("run", "Simplified view", 11);
                    ClientUtilspsiblast.marshallToXML(parameter, debugLevel, "parameters");
                } else {
                    printDebugMessage("run", "Returned WsParameter object is null", 41);
                }
            }

            // Submit new job
            else if (cli.hasOption("email") && (sequence != null || (asequence != null && bsequence != null))) {

                String jobid = null;
                if (sequence != null){
                    jobid = submitJob(cli, ClientUtilspsiblast.loadData(sequence), null);
                } else if (asequence != null && bsequence != null) {
                    jobid = submitJob(cli, ClientUtilspsiblast.loadData(asequence),
                                             ClientUtilspsiblast.loadData(bsequence));
                }

                if (jobid != null) {
                    // Asynchronous (default) execution
                    if (cli.hasOption("async")) {
                        System.out.println(jobid);
                        printDebugMessage("run", String.format("You can check status of your job with:\n" +
                                                 "java -jar psiblast.jar --status --jobid %s\n", jobid), 11);

                        if (outputLevel > 0)
                            System.out.println("To check status: java -jar psiblast.jar --status --jobid " + jobid);
                        
                    // Synchronous execution
                    } else {
                        if (outputLevel > 0){
                            System.out.println("JobId: " + jobid);
                        } else {
                            System.out.println(jobid);
                        }
                        printDebugMessage("run", String.format("JobId: %s", jobid), 11);
                        if (ClientUtilspsiblast.getStatusInIntervals(this, jobid, pollFreq, outputLevel, debugLevel)) {
                            downloadResults(cli, jobid);
                        } else {
                            System.out.println("Job failed.");
                            printDebugMessage("run", "Job failed.", 41);
                        }
                    }
                } else {
                    printDebugMessage("run", "Job submission failed.", 41);
                }
            }

            // Check already submitted job
            else if (cli.hasOption("jobid") && cli.hasOption("status")) {
                String jobid = cli.getOptionValue("jobid");
                printGetStatus(jobid);
            }

            else if (cli.hasOption("jobid") && (cli.hasOption("resultTypes") || cli.hasOption("polljob"))) {
                String jobid = cli.getOptionValue("jobid");
                final String status = checkStatus(jobid);
                if (status == "PENDING" || status == "RUNNING"){
                    System.out.println("Error: Job status is " + status
                                       + ". To get result types the job must be finished.");
                    System.exit(0);
                }

                // Check available result types
                if (cli.hasOption("resultTypes")) {
                    if (outputLevel > 0)
                        System.out.println("Getting result types for job " + jobid);
                    if (outputLevel > 0)
                        System.out.println("Available result types:");
                    ClientUtilspsiblast.marshallToXML(getResultTypesForJobId(jobid), debugLevel, "types");
                    if (outputLevel > 0)
                        System.out.println("To get results:\n  java -jar psiblast.jar --polljob --jobid " + jobid
                                           + "\n  java -jar psiblast.jar --polljob --outformat <type> --jobid " + jobid);
                }

                // Download (all/filtered) result files
                else if (cli.hasOption("polljob")) {

                    downloadResults(cli, jobid);
                } else {
                    printDebugMessage("run", "Please specify one of following parameters: --status, --resultTypes, --polljob.", 41);
                }
            } else {
                printUsage();
                printDebugMessage("run", "\n\nYou either have to specify both --email and --sequence or a --jobid value. \n\nTo learn more run: java -jar psiblast.jar --help", 41);
                return;
            }
        } catch (ParseException e) {
            System.out.println("Error: unrecognised argument combination: " + e.getMessage());
            printDebugMessage("run", e.getMessage(), 41);
        } catch (InterruptedException e) {
            printDebugMessage("run", e.getMessage(), 41);
        } catch (ServiceException e) {
            printDebugMessage("run", e.getMessage(), 41);
        } catch (JAXBException e) {
            printDebugMessage("run", e.getMessage(), 41);
        } catch (IOException e) {
            printDebugMessage("run", e.getMessage(), 41);
        }

    }


    /**
     * Download result files to specified directory
     *
     * @param cli
     * @param jobid
     * @throws IOException
     */
    public void downloadResults(CommandLine cli, String jobid) throws IOException {
        List<WsResultType> allResultTypes = getResultTypesForJobId(jobid).getAsList();
        List<WsResultType> resultTypes;

        outputLevel = getOutputLevel();
        if (outputLevel > 1)
            System.out.println("Getting results for job " + jobid);
        // Augment user specified result types with file suffix
        if (cli.hasOption("outformat")) {
            resultTypes = ClientUtilspsiblast.processUserOutputFormats(cli.getOptionValue("outformat"),
                allResultTypes, debugLevel);
        } else {
            resultTypes = allResultTypes;
        }

        // Iterate over filtered result types and save each result in a separate file
        for (WsResultType resultType : resultTypes) {

            String identifier = resultType.getIdentifier();
            if (outputLevel > 1)
                System.out.println("Getting " + identifier);
            String fileSuffix = resultType.getFileSuffix();
            String outputFile = jobid + "-" + identifier + "." + fileSuffix;
            ClientResponse response = getResponse(baseUrl, "/result/" + jobid + "/" + identifier, RequestType.GET, null);

            InputStream inputStream = response.getEntity(InputStream.class);
            Files.copy(inputStream, Paths.get(outputFile));
            if (outputLevel > 0)
                System.out.println("Creating result file: " + outputFile);
            printDebugMessage("downloadResults", String.format("Download: %s", outputFile), 11);

        }
    }


    /**
     * Submit job for a given sequence
     *
     * @param cli
     * @param inputSeq
     * @return
     * @throws ServiceException
     * @throws IOException
     */
    private String submitJob(CommandLine cli, String inputSeq, String inputSeq2)
            throws ServiceException, IOException {

        Form form = new Form();
        if (inputSeq2 != null) {
            form.putSingle("asequence", inputSeq);
            form.putSingle("bsequence", inputSeq2);
        } else {
            form.putSingle("sequence", inputSeq);
        }

        for (Option option : cli.getOptions()) {
            String optionName = option.getOpt();
            String optionType = option.getLongOpt();
            String optionValue = option.getValue();

            if (optionName.equals("sequence") || optionName.equals("async")) {
                continue;
            }

            if (optionType.equals("ArrayOfString")) {
                form.put(optionName, Arrays.asList(optionValue.split(",")));
            } else if (optionType.equals("boolean")) {
                optionValue = optionValue.toLowerCase();
                switch (optionValue) {
                    case "true":
                    case "yes":
                    case "y":
                        optionValue = "true";
                        break;
                    case "false":
                    case "no":
                    case "n":
                        optionValue = "false";
                        break;
                    default:
                        printDebugMessage("submitJob", "Boolean parameter can be only 'true' or 'false'", 31);
                        System.exit(0);
                }
                form.putSingle(optionName, optionValue);

            } else {
                form.putSingle(optionName, optionValue);
            }
        }

        // Pass default values and fix bools (without default value)
        if (cli.hasOption("matrix") == false)
           form.putSingle("matrix", "BLOSUM62");

        if (cli.hasOption("gapopen") == false)
           form.putSingle("gapopen", "11");

        if (cli.hasOption("gapext") == false)
           form.putSingle("gapext", "1");

        if (cli.hasOption("expthr") == false)
           form.putSingle("expthr", "10.0");

        if (cli.hasOption("psithr") == false)
           form.putSingle("psithr", "1.0e-3");

        if (cli.hasOption("scores") == false)
           form.putSingle("scores", "500");

        if (cli.hasOption("alignments") == false)
           form.putSingle("alignments", "500");

        if (cli.hasOption("dropoff") == false)
           form.putSingle("dropoff", "15");

        if (cli.hasOption("finaldropoff") == false)
           form.putSingle("finaldropoff", "25");

        if (cli.hasOption("filter") == false)
           form.putSingle("filter", "F");



        ClientResponse response = getResponse(baseUrl, "/run", RequestType.POST, form);

        if (ClientUtilspsiblast.isResponseCorrect(response, debugLevel)) {
            return response.getEntity(String.class);
        }
        return null;
    }

    private ClientResponse getResponse(String baseUrl, String urlSuffix, RequestType requestType, Form form) {

        WebResource target = getClient()
                .resource(baseUrl + urlSuffix);

        printDebugMessage("getResponse", requestType + " " + target.toString(), 11);

        try {
            if (requestType == RequestType.GET) {
                return target.get(ClientResponse.class);

            } else if (requestType == RequestType.POST) {

                printDebugMessage("getResponse", "POST parameters:", 11);
                form.keySet().stream().forEach(s -> {
                    String parameters = form.get(s).toString();

                    if (parameters.length() > 20) {
                        parameters = parameters.substring(0, 20) + " ...";
                    }

                    printDebugMessage("getResponse", s + "\t" + parameters, 11);
                });

                //System.out.println();
                return target.post(ClientResponse.class, form);
            } else {
                printDebugMessage("getResponse", "Unsupported RequestType", 41);
            }
        } catch (Exception e) {
            printDebugMessage("getResponse", "Problem with Internet connection", 41);
            printDebugMessage("getResponse", e.getMessage(), 41);
        }
        return null;
    }

    public enum RequestType {GET, POST}
}