package com.halodoc.oms.orders.slack;

import io.restassured.RestAssured;
import org.testng.ITestContext;
import org.testng.Reporter;
import org.testng.TestListenerAdapter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import static com.halodoc.oms.orders.utilities.constants.Constants.S3_BUCKET_URL;
import static com.halodoc.oms.orders.utilities.constants.Constants.SLACK_HOOK;

public class SlackStageListener extends TestListenerAdapter {

	public static int passTests=0,failedTests=0,allTests=0,skippedTests;
	public static String failedTestCasesNames;

	public static HashMap<String, String> HOOKS = new HashMap<String, String>() {
		{
			put("backend_automation", "T0QP5NN4W/B01D8SP8S1E/rUcWTg3CuD6UahKVw09AldnV");
			put("testReports", "T0QP5NN4W/B020PM5Q81Y/gkHzcKbEwbiTIwyG1mithEAT");
			put("dop_automation", "T0QP5NN4W/B026VH1FVR6/nn1WM8Ks3lOSGJix5uoUIxJz");
		}
	};

	@Override
	public void onFinish(ITestContext testContext) {
		//String slackChannel = "automationreport";
		String tag = "stage";
		passTests += testContext.getPassedTests().size();
		skippedTests += testContext.getSkippedTests().size();
		failedTests += testContext.getFailedTests().size();
		allTests += testContext.getPassedTests().size() + testContext.getFailedTests().size() + testContext.getSkippedTests().size();
		failedTestCasesNames = testContext.getFailedTests().getAllMethods().stream().map(m -> m.getMethodName()).collect(Collectors.joining("\n"));

			Reporter.log("Send Notification to slack", true);
			String status = failedTests > 0 || passTests == 0 ? "#FF0000" : "#36a64f";

			SlackMessage slackMessage = new SlackMessage();
			List<Attachment> attachments = new ArrayList<>();
			Attachment attachment = new Attachment();

			//attachment.setImage_url("https://is2-ssl.mzstatic.com/image/thumb/Purple114/v4/5f/ee/86/5fee8660-deb0-69bb-77bc-cfd30e8fcba2/source/256x256bb.jpg");
			attachment.setPretext(String.format(testContext.getSuite().getName()+" Automation Report"));
			attachment.setColor(status);
			attachment.setText("\nTest Suite Results");
			//attachment.setThumb_url("http://example.com/path/to/thumb.png");

			Attachment.Fields allTestsFields = attachment.new Fields();
			allTestsFields.setTitle("Total Tests");
			allTestsFields.setValue(String.valueOf(allTests));
			allTestsFields.setShort_(true);

			Attachment.Fields passFields = attachment.new Fields();
			passFields.setTitle("Passed");
			passFields.setValue(String.valueOf(passTests));
			passFields.setShort_(true);

			Attachment.Fields failedFields = attachment.new Fields();
			failedFields.setTitle("Failed");
			failedFields.setValue(String.valueOf(failedTests));
			failedFields.setShort_(true);

			Attachment.Fields skippedFields = attachment.new Fields();
			skippedFields.setTitle("Skipped");
			skippedFields.setValue(String.valueOf(skippedTests));
			skippedFields.setShort_(true);

			Attachment.Fields failedTestCasesFeilds = attachment.new Fields();
			skippedFields.setTitle("Failed Test cases Names");
			skippedFields.setValue(failedTestCasesNames);
			skippedFields.setShort_(true);

			Attachment.Fields allureFields = attachment.new Fields();
			allureFields.setTitle("Allure Report");
			try {
				ZoneId zoneid2 = ZoneId.of("Asia/Bangkok");
				LocalTime id2 = LocalTime.now(zoneid2);
				StringBuffer sb = new StringBuffer(id2.toString());

				//allureFields.setValue(String.format("https://jenkins.devops.mhealth.tech/view/BackendTests/job/ET-tcRecon-backTest-prod-validation/ws/allure-report/ui/index.html"));
				allureFields.setValue(String.format(S3_BUCKET_URL + LocalDate.now() + "-" + sb.substring(0, 2) + "/index.html"));
			} catch (Exception e) {
				Reporter.log("Failed to get latest run report number : " + e.getMessage(), true);
				e.printStackTrace();
			}
			allureFields.setShort_(true);

			List<Attachment.Fields> fields = new ArrayList<>();
			fields.add(allTestsFields);
			fields.add(passFields);
			fields.add(failedFields);
			fields.add(skippedFields);
			fields.add(allureFields);
			if(!failedTestCasesNames.isEmpty()) {
				fields.add(failedTestCasesFeilds);
			}
			attachment.setFields(fields);
			attachments.add(attachment);

			slackMessage.setAttachments(attachments);
			sendSlackNotifications(slackMessage, "dop_automation");
	}

	public void sendSlackNotifications(SlackMessage slackMessage, String slackChannel) {
		RestAssured.given().header("content-type", "application/json").when().body(slackMessage)
		           .post(SLACK_HOOK + HOOKS.get(slackChannel));
	}
}