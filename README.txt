In order to run the auction system on your computer you must:
    1. Give executable permissions to the prepare.sh script by invoking 'chmod +x prepare.sh'
    2. Execute the prepare.sh script which will make runClient, runServer and runTester executable
    3. Execute the Makefile supplied by issuing make in the src folder
    4. Execute the runServer script with an argument the ip address of the curent computer or localhost if intending to run locally. for eg './runServer.sh localhost'
    5. Execute the runClient script with an argument the ip address of the server computer or localhost if intending to run locally. for eg './runClient.sh localhost'
    6. (Optional) If you want to run the tester you can invoke runTester with an argument the ip address or localhost and the mode ranging (1-5) described in the report.
    