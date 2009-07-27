# ======================================================================
#
# Test sample SOAP::Lite clients run.
#
# ======================================================================

#PERL = perl
PERL = /ebi/extserv/bin/perl/bin/perl
#PERL = /sw/arch/bin/perl
#EMAIL = email@example.org
EMAIL = support@ebi.ac.uk

# Run all test sets
all: clustalw2 ebeye fasta kalign mafft muscle ncbiblast psisearch tcoffee wublast

clean: clustalw2_clean ebeye_clean fasta_clean kalign_clean mafft_clean muscle_clean ncbiblast_clean psisearch_clean tcoffee_clean wublast_clean

# ClustalW 2.0.x
clustalw2: clustalw2_params clustalw2_param_detail clustalw2_align clustalw2_align_stdin_stdout

clustalw2_params:
	${PERL} clustalw2_lwp.pl --params

clustalw2_param_detail:
	${PERL} clustalw2_lwp.pl --paramDetail alignment

clustalw2_align:
	${PERL} clustalw2_lwp.pl --email ${EMAIL} ../test_data/multi_prot.tfa

clustalw2_align_stdin_stdout:
	cat ../test_data/multi_prot.tfa | ${PERL} clustalw2_lwp.pl --email ${EMAIL} --quiet --outformat aln-clustalw --outfile - - > clustalw2-blah.aln

clustalw2_clean:
	rm -f clustalw2-*

# EB-eye
ebeye: ebeye_listDomains ebeye_getNumberOfResults ebeye_getResultsIds ebeye_getAllResultsIds ebeye_listFields ebeye_getResults ebeye_getEntry \
ebeye_getEntries ebeye_getEntryFieldUrls ebeye_getEntriesFieldUrls ebeye_getDomainsReferencedInDomain ebeye_getDomainsReferencedInEntry \
ebeye_listAdditionalReferenceFields ebeye_getReferencedEntries ebeye_getReferencedEntriesSet ebeye_getReferencedEntriesFlatSet \
ebeye_getDomainsHierarchy ebeye_getDetailledNumberOfResult ebeye_listFieldsInformation

ebeye_listDomains:
	${PERL} ebeye_lwp.pl --listDomains

ebeye_getNumberOfResults:
	${PERL} ebeye_lwp.pl --getNumberOfResults uniprot 'azurin'

ebeye_getResultsIds:
	${PERL} ebeye_lwp.pl --getResultsIds uniprot 'azurin' 1 10

ebeye_getAllResultsIds:
	${PERL} ebeye_lwp.pl --getAllResultsIds uniprot 'azurin'

ebeye_listFields:
	${PERL} ebeye_lwp.pl --listFields uniprot

ebeye_getResults:
	${PERL} ebeye_lwp.pl --getResults uniprot 'azurin' 'id,acc,name,status' 1 10

ebeye_getEntry:
	${PERL} ebeye_lwp.pl --getEntry uniprot 'WAP_RAT' 'id,acc,name,status'

ebeye_getEntries:
	${PERL} ebeye_lwp.pl --getEntries uniprot 'WAP_RAT,WAP_MOUSE' 'id,acc,name,status'

ebeye_getEntryFieldUrls:
	${PERL} ebeye_lwp.pl --getEntryFieldUrls uniprot 'WAP_RAT' 'id'

ebeye_getEntriesFieldUrls:
	${PERL} ebeye_lwp.pl --getEntriesFieldUrls uniprot 'WAP_RAT,WAP_MOUSE' 'id'

ebeye_getDomainsReferencedInDomain:
	${PERL} ebeye_lwp.pl --getDomainsReferencedInDomain uniprot

ebeye_getDomainsReferencedInEntry:
	${PERL} ebeye_lwp.pl --getDomainsReferencedInEntry uniprot 'WAP_RAT'

ebeye_listAdditionalReferenceFields:
	${PERL} ebeye_lwp.pl --listAdditionalReferenceFields uniprot

ebeye_getReferencedEntries:
	${PERL} ebeye_lwp.pl --getReferencedEntries uniprot 'WAP_RAT' interpro

ebeye_getReferencedEntriesSet:
	${PERL} ebeye_lwp.pl --getReferencedEntriesSet uniprot 'WAP_RAT,WAP_MOUSE' interpro 'id,name'

ebeye_getReferencedEntriesFlatSet:
	${PERL} ebeye_lwp.pl --getReferencedEntriesFlatSet uniprot 'WAP_RAT,WAP_MOUSE' interpro 'id,name'

ebeye_getDomainsHierarchy:
	${PERL} ebeye_lwp.pl --getDomainsHierarchy

ebeye_getDetailledNumberOfResult: ebeye_getDetailledNumberOfResult_flat ebeye_getDetailledNumberOfResult_tree

ebeye_getDetailledNumberOfResult_flat:
	${PERL} ebeye_lwp.pl --getDetailledNumberOfResult allebi 'azurin' true

ebeye_getDetailledNumberOfResult_tree:
	${PERL} ebeye_lwp.pl --getDetailledNumberOfResult allebi 'azurin' false

ebeye_listFieldsInformation:
	${PERL} ebeye_lwp.pl --listFieldsInformation uniprot

ebeye_clean:

# FASTA
fasta: fasta_params fasta_param_detail fasta_file fasta_dbid fasta_stdin_stdout fasta_id_list_file fasta_multifasta_file

fasta_params:
	${PERL} fasta_lwp.pl --params

fasta_param_detail:
	${PERL} fasta_lwp.pl --paramDetail program

fasta_file:
	${PERL} fasta_lwp.pl --email ${EMAIL} --program fasta --database uniprotkb_swissprot --eupper 1.0 --scores 10 --alignments 10 --stype protein ../test_data/SWISSPROT_ABCC9_HUMAN.fasta

fasta_dbid:
	${PERL} fasta_lwp.pl --email ${EMAIL} --program fasta --database uniprotkb_swissprot --eupper 1.0 --scores 10 --alignments 10 --stype protein UNIPROT:ABCC9_HUMAN

fasta_stdin_stdout:
	cat ../test_data/SWISSPROT_ABCC9_HUMAN.fasta | ${PERL} fasta_lwp.pl --email ${EMAIL} --program fasta --database uniprotkb_swissprot --eupper 1.0 --scores 10 --alignments 10 --stype protein --quiet --outformat out --outfile - - > fasta-blah.txt

fasta_id_list_file:
	${PERL} fasta_lwp.pl --email ${EMAIL} --program fasta --database uniprotkb_swissprot --eupper 1.0 --scores 10 --alignments 10 --stype protein --outformat ids --outfile - @../test_data/uniprot_id_list.txt

fasta_multifasta_file:
	${PERL} fasta_lwp.pl --email ${EMAIL} --program fasta --database uniprotkb_swissprot --eupper 1.0 --scores 10 --alignments 10 --stype protein --outformat ids --outfile - --multifasta  ../test_data/multi_prot.tfa

fasta_clean:
	rm -f fasta-*

# Kalign
kalign: kalign_params kalign_param_detail kalign_file kalign_stdin_stdout

kalign_params:
	${PERL} kalign_lwp.pl --params

kalign_param_detail:
	${PERL} kalign_lwp.pl --paramDetail format

kalign_file:
	${PERL} kalign_lwp.pl --email ${EMAIL} --stype protein ../test_data/multi_prot.tfa

kalign_stdin_stdout:
	cat ../test_data/multi_prot.tfa | ${PERL} kalign_lwp.pl --email ${EMAIL} --stype protein --quiet --outformat aln-clustalw --outfile - - > kalign-blah.aln

kalign_clean:
	rm -f kalign-*

# MAFFT
mafft: mafft_params mafft_param_detail mafft_file mafft_stdin_stdout

mafft_params:
	${PERL} mafft_lwp.pl --params

mafft_param_detail:
	${PERL} mafft_lwp.pl --paramDetail matrix

mafft_file:
	${PERL} mafft_lwp.pl --email ${EMAIL} ../test_data/multi_prot.tfa

mafft_stdin_stdout:
	cat ../test_data/multi_prot.tfa | ${PERL} mafft_lwp.pl --email ${EMAIL} --quiet --outformat out --outfile - - > mafft-blah.aln

mafft_clean:
	rm -f mafft-*

# MUSCLE
muscle: muscle_params muscle_param_detail muscle_file muscle_stdin_stdout

muscle_params:
	${PERL} muscle_lwp.pl --params

muscle_param_detail:
	${PERL} muscle_lwp.pl --paramDetail format

muscle_file:
	${PERL} muscle_lwp.pl --email ${EMAIL} --output clw ../test_data/multi_prot.tfa

muscle_stdin_stdout:
	cat ../test_data/multi_prot.tfa | ${PERL} muscle_lwp.pl --email ${EMAIL} --output clw --quiet --outformat out --outfile - - > muscle-blah.aln

muscle_clean:
	rm -f muscle-*

# NCBI BLAST
ncbiblast: ncbiblast_params ncbiblast_param_detail ncbiblast_file ncbiblast_dbid ncbiblast_stdin_stdout ncbiblast_id_list_file ncbiblast_multifasta_file

ncbiblast_params:
	${PERL} ncbiblast_lwp.pl --params

ncbiblast_param_detail:
	${PERL} ncbiblast_lwp.pl --paramDetail program

ncbiblast_file:
	${PERL} ncbiblast_lwp.pl --email ${EMAIL} --program blastp --database uniprotkb_swissprot --scores 10 --alignments 10 --stype protein ../test_data/SWISSPROT_ABCC9_HUMAN.fasta

ncbiblast_dbid:
	${PERL} ncbiblast_lwp.pl --email ${EMAIL} --program blastp --database uniprotkb_swissprot --scores 10 --alignments 10 --stype protein UNIPROT:ABCC9_HUMAN

ncbiblast_stdin_stdout:
	cat ../test_data/SWISSPROT_ABCC9_HUMAN.fasta | ${PERL} ncbiblast_lwp.pl --email ${EMAIL} --program blastp --database uniprotkb_swissprot --scores 10 --alignments 10 --stype protein --quiet --outformat out --outfile - - > ncbiblast-blah.txt

ncbiblast_id_list_file:
	${PERL} ncbiblast_lwp.pl --email ${EMAIL} --program blastp --database uniprotkb_swissprot --scores 10 --alignments 10 --stype protein --outformat ids --outfile - @../test_data/uniprot_id_list.txt

ncbiblast_multifasta_file:
	${PERL} ncbiblast_lwp.pl --email ${EMAIL} --program blastp --database uniprotkb_swissprot --scores 10 --alignments 10 --stype protein --outformat ids --outfile - --multifasta  ../test_data/multi_prot.tfa

ncbiblast_clean:
	rm -f ncbiblast-*

# PSI-BLAST
#psiblast: psiblast_file psiblast_dbid psiblast_stdin_stdout
#
#psiblast_file:
#	${PERL} psiblast.pl --email ${EMAIL} --database swissprot --scores 10 --align 10 ../test/SWISSPROT_ABCC9_HUMAN.fasta
#
#psiblast_dbid:
#	${PERL} psiblast.pl --email ${EMAIL} --database swissprot --scores 10 --align 10 SWISSPROT:ABCC9_HUMAN
#
#psiblast_stdin_stdout:
#	cat ../test/SWISSPROT_ABCC9_HUMAN.fasta | ${PERL} psiblast.pl --email ${EMAIL} --database swissprot --scores 10 --align 10 --quiet --outformat tooloutput --outfile - - > psiblast-blah.txt
#
#psiblast_clean:
#	rm -f psiblast-*

# PSI-Search
psisearch: psisearch_params psisearch_param_detail psisearch_file psisearch_dbid psisearch_stdin_stdout

psisearch_params:
	${PERL} psisearch_lwp.pl --params

psisearch_param_detail:
	${PERL} psisearch_lwp.pl --paramDetail matrix

psisearch_file:
	${PERL} psisearch_lwp.pl --email ${EMAIL} --database uniprotkb_swissprot --scores 10 --align 10 ../test_data/SWISSPROT_ABCC9_HUMAN.fasta

psisearch_dbid:
	${PERL} psisearch_lwp.pl --email ${EMAIL} --database uniprotkb_swissprot --scores 10 --align 10 UNIPROT:ABCC9_HUMAN

psisearch_stdin_stdout:
	cat ../test_data/SWISSPROT_ABCC9_HUMAN.fasta | ${PERL} psisearch_lwp.pl --email ${EMAIL} --database uniprotkb_swissprot --scores 10 --align 10 --quiet --outformat tooloutput --outfile - - > psisearch-blah.txt

psisearch_clean:
	rm -f psisearch-*

# T-Coffee
tcoffee: tcoffee_params tcoffee_param_detail tcoffee_file tcoffee_stdin_stdout

tcoffee_params:
	${PERL} tcoffee_lwp.pl --params

tcoffee_param_detail:
	${PERL} tcoffee_lwp.pl --paramDetail matrix

tcoffee_file:
	${PERL} tcoffee_lwp.pl --email ${EMAIL} ../test_data/multi_prot.tfa

tcoffee_stdin_stdout:
	cat ../test_data/multi_prot.tfa | ${PERL} tcoffee_lwp.pl --email ${EMAIL} --quiet --outformat out --outfile - - > tcoffee-blah.aln

tcoffee_clean:
	rm -f tcoffee-*

# WU-BLAST
wublast: wublast_params wublast_param_detail wublast_file wublast_dbid wublast_stdin_stdout

wublast_params:
	${PERL} wublast_lwp.pl --params

wublast_param_detail:
	${PERL} wublast_lwp.pl --paramDetail program

wublast_file:
	${PERL} wublast_lwp.pl --email ${EMAIL} --program blastp --database uniprotkb_swissprot --scores 10 --alignments 10 --stype protein ../test_data/SWISSPROT_ABCC9_HUMAN.fasta

wublast_dbid:
	${PERL} wublast_lwp.pl --email ${EMAIL} --program blastp --database uniprotkb_swissprot --scores 10 --alignments 10 --stype protein UNIPROT:ABCC9_HUMAN

wublast_stdin_stdout:
	cat ../test_data/SWISSPROT_ABCC9_HUMAN.fasta | ${PERL} wublast_lwp.pl --email ${EMAIL} --program blastp --database uniprotkb_swissprot --scores 10 --alignments 10 --stype protein --quiet --outformat out --outfile - - > wublast-blah.txt

wublast_clean:
	rm -f wublast-*
