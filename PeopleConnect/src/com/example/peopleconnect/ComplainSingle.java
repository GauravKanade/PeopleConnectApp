package com.example.peopleconnect;

import java.util.StringTokenizer;

import org.apache.http.Header;
import org.json.JSONException;
import org.json.JSONObject;

import com.loopj.android.http.*;
import com.loopj.android.image.SmartImageView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class ComplainSingle extends Activity implements OnClickListener {
	int id = 1, n;
	int user_id = Utility.user_id;
	ProgressDialog progress;
	Context context = this;
	TextView tvsender, tvarea, tvtime, tvdescription, tvtitle, tvCategory,
			tvStatus, tvComplaintId;
	String sender, area, time, description, title, location, status, category,
			image;
	LinearLayout ll1;
	String comments[];
	String comment_times[];
	String user_names[];
	int user_ids_comments[];
	int comments_ids[];

	String actions[], authorities[], action_time[];
	SmartImageView myImage;
	EditText etComment;
	Button bPostComment;
	NotificationManager nm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_complain_single);
		progress = ProgressDialog.show(ComplainSingle.this, "Please Wait",
				"Getting the Complaint Details");
		init();
	}

	private void init() {
		Bundle data = getIntent().getExtras();
		id = Integer.parseInt(data.getString("complaint_id"));
		sender = data.getString("sender");
		time = data.getString("time");
		area = data.getString("area");
		title = data.getString("title");

		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.cancel(Utility.StatusNotificationId);
		tvsender = (TextView) findViewById(R.id.tvSenderName_ComplainSingle);
		tvarea = (TextView) findViewById(R.id.tvArea_ComplainSingle);
		tvtime = (TextView) findViewById(R.id.tvTime_ComplainSingle);
		tvdescription = (TextView) findViewById(R.id.tvDescription_ComplaintSingle);
		tvtitle = (TextView) findViewById(R.id.tvHeading_ComplainSingle);
		tvCategory = (TextView) findViewById(R.id.tvCategory_ComplainSingle);
		tvStatus = (TextView) findViewById(R.id.tvStatus_ComplainSingle);
		tvComplaintId = (TextView) findViewById(R.id.tvComplaintID_ComplainSingle);
		ll1 = (LinearLayout) findViewById(R.id.llCommentContainer);
		myImage = (SmartImageView) this.findViewById(R.id.my_image);
		etComment = (EditText) findViewById(R.id.etCommentDescriptionComplaintSingle);
		bPostComment = (Button) findViewById(R.id.bPostComment_ComplaintSingle);

		bPostComment.setOnClickListener(this);

		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.put("complaint_id", id);
		try {
			client.post(Utility.getURL(this) + "GetSingleComplaint", params,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							// Toast.makeText(context, new String(arg2),
							// Toast.LENGTH_LONG).show();
							StringTokenizer st1 = new StringTokenizer(
									new String(arg2), "$%#$");
							description = st1.nextToken();
							location = st1.nextToken();
							status = st1.nextToken();
							category = st1.nextToken();
							image = st1.nextToken();

							myImage.setImageUrl(Utility.getURL(context) + image);
							tvtitle.setText(title);
							tvStatus.setText(status);
							tvarea.setText("Location: " + location);
							tvsender.setText(sender);
							tvtime.setText(time);
							tvCategory.setText("Category: " + category);
							tvComplaintId.setText("Complaint: #" + id);
							tvdescription.setText(description);

						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							Toast.makeText(context, arg3.getMessage(),
									Toast.LENGTH_LONG).show();
						}
					});
		} catch (Exception e) {
			// TODO: handle exception
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		// Put Image
		AsyncHttpClient client2 = new AsyncHttpClient();
		RequestParams params2 = new RequestParams();
		params2.put("complaint_id", id);
		try {
			client2.post(Utility.getURL(context) + "GetComments", params2,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							String response = new String(arg2);

							StringTokenizer st1 = new StringTokenizer(response,
									"####");
							n = st1.countTokens();

							comments = new String[n];
							comment_times = new String[n];
							user_names = new String[n];
							user_ids_comments = new int[n];
							comments_ids = new int[n];
							for (int i = 0; i < n; i++) {
								String temp = st1.nextToken();
								StringTokenizer st2 = new StringTokenizer(temp,
										"$$$$");
								comments_ids[i] = Integer.parseInt(st2
										.nextToken());
								comments[i] = st2.nextToken();
								comment_times[i] = st2.nextToken();
								user_ids_comments[i] = Integer.parseInt(st2
										.nextToken());
								user_names[i] = st2.nextToken();
							}
							for (int i = 0; i < comments.length; i++) {
								LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
										800, LayoutParams.WRAP_CONTENT);
								layoutparams.setMargins(10, 10, 10, 10);
								LinearLayout singleCommentContainer = new LinearLayout(
										context);
								singleCommentContainer
										.setOrientation(LinearLayout.VERTICAL);
								final int comment_layout_id = 2 * n + i;
								singleCommentContainer.setId(comment_layout_id);

								LinearLayout ll3 = new LinearLayout(context);
								ll3.setLayoutParams(layoutparams);
								ll3.setPadding(5, 5, 5, 5);
								ll3.setOrientation(LinearLayout.VERTICAL);

								// Sender
								TextView tvSender = new TextView(context);
								tvSender.setText(user_names[i]);
								tvSender.setTypeface(null, Typeface.BOLD);
								tvSender.setPadding(10, 10, 10, 10);

								// Time
								TextView tvTime = new TextView(context);
								tvTime.setText(comment_times[i]);
								tvTime.setTypeface(null, Typeface.ITALIC);
								tvTime.setPadding(0, 0, 10, 0);
								tvTime.setGravity(Gravity.RIGHT);

								// comment body
								final TextView tvComment = new TextView(context);
								tvComment.setText(comments[i]);
								tvComment.setLayoutParams(layoutparams);
								tvComment.setPadding(10, 10, 10, 10);
								final int iF = i;
								// Edit Button
								ImageButton ibEdit = new ImageButton(context);
								ibEdit.setImageResource(R.drawable.edit);
								ibEdit.setLayoutParams(new LinearLayout.LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT));
								ibEdit.setId(i);
								ibEdit.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										LayoutInflater li = LayoutInflater
												.from(context);
										View promptsView = li.inflate(
												R.layout.layout_edit_comment,
												null);

										AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
												context);

										// set prompts.xml to alert dialog
										// builder
										alertDialogBuilder.setView(promptsView);

										final EditText userInput = (EditText) promptsView
												.findViewById(R.id.editTextDialogUserInput);
										userInput.setText(comments[iF]);
										// set dialog message
										alertDialogBuilder
												.setCancelable(false)
												.setPositiveButton(
														"Confirm",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int id) {

																String newCom = userInput
																		.getText()
																		.toString();
																if (newCom
																		.length() == 0) {
																	Toast.makeText(
																			context,
																			"Please enter a comment",
																			Toast.LENGTH_SHORT)
																			.show();
																} else {
																	tvComment
																			.setText(newCom);
																	comments[iF] = newCom;
																	editComment(
																			comments_ids[iF],
																			newCom);
																	progress = ProgressDialog
																			.show(ComplainSingle.this,
																					"Wait",
																					"Updating Comment...");
																}
															}

														})
												.setNegativeButton(
														"Cancel",
														new DialogInterface.OnClickListener() {
															public void onClick(
																	DialogInterface dialog,
																	int id) {
																dialog.cancel();
															}
														});

										AlertDialog alertDialog = alertDialogBuilder
												.create();
										alertDialog.show();
									}
								});

								// Delete Button
								ImageButton ibDelete = new ImageButton(context);
								ibDelete.setImageResource(R.drawable.action_delete);
								ibDelete.setLayoutParams(new LinearLayout.LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT));
								ibDelete.setId(n + i);

								ibDelete.setOnClickListener(new OnClickListener() {

									@Override
									public void onClick(View v) {
										AlertDialog.Builder builder = new AlertDialog.Builder(
												ComplainSingle.this);
										// Add the buttons
										builder.setPositiveButton(
												"Confirm ",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id1) {
														// User clicked OK
														// button
														progress = ProgressDialog
																.show(ComplainSingle.this,
																		"Wait",
																		"Deleting...");
														deleteComment(comments_ids[iF]);
														ll1.removeViewAt(iF);
													}
												});
										builder.setNegativeButton(
												"Cancel",
												new DialogInterface.OnClickListener() {
													public void onClick(
															DialogInterface dialog,
															int id) {
														// User cancelled the
														// dialog
													}
												});
										// Set other dialog properties
										builder.setTitle("Confirm??");
										builder.setMessage("Are you sure you want to delete this comment??");
										builder.setIcon(R.drawable.ic_action_help);
										builder.show();

									}
								});

								// Top Line Holder
								LinearLayout ll4 = new LinearLayout(context);
								ll4.setLayoutParams(layoutparams);
								if (user_ids_comments[i] == user_id) {
									singleCommentContainer
											.setGravity(Gravity.RIGHT);
									ll4.addView(ibEdit);
									ll4.addView(ibDelete);
								}

								ll4.setGravity(Gravity.RIGHT);
								ll4.setOrientation(LinearLayout.HORIZONTAL);
								ll4.setPadding(0, 0, 13, 0);
								RelativeLayout rl = new RelativeLayout(context);
								rl.setLayoutParams(new LinearLayout.LayoutParams(
										LayoutParams.WRAP_CONTENT,
										LayoutParams.WRAP_CONTENT));
								rl.addView(tvSender);
								rl.addView(ll4);

								ll3.addView(rl);
								ll3.addView(tvComment);
								ll3.addView(tvTime);
								ll3.setBackgroundResource(R.drawable.comment_layout);

								// add components to singleCommentContainer

								singleCommentContainer.setPadding(5, 5, 5, 5);
								singleCommentContainer.addView(ll3);
								ll1.addView(singleCommentContainer);
							}

						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							Toast.makeText(context, arg3.getMessage(),
									Toast.LENGTH_LONG).show();

						}
					});
		} catch (Exception e) {
			progress.dismiss();
			Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
		}
		try {
			AsyncHttpClient client3 = new AsyncHttpClient();
			RequestParams params3 = new RequestParams();
			params3.put("complaint_id", id);
			client3.post(Utility.getURL(context) + "GetActions", params3,
					new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							String response = new String(arg2);
							JSONObject data;
							try {
								data = new JSONObject(response);

								int n = data.getInt("n");
								actions = new String[n];
								action_time = new String[n];
								authorities = new String[n];

								for (int i = 0; i < n; i++) {
									actions[i] = data.getString(i + "action");
									action_time[i] = data.getString(i + "time");
									authorities[i] = data.getString(i
											+ "authority");
								}
								LinearLayout llActions = (LinearLayout) findViewById(R.id.llActionsComplaintSingles);

								for (int i = 0; i < n; i++) {
									LinearLayout llActionHolder = new LinearLayout(
											context);
									LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											LayoutParams.WRAP_CONTENT);
									llActionHolder
											.setOrientation(LinearLayout.VERTICAL);

									llActionHolder
											.setLayoutParams(layoutparams);
									TextView tvTimeAction = new TextView(
											context);
									tvTimeAction.setLayoutParams(layoutparams);
									tvTimeAction.setGravity(Gravity.RIGHT);
									tvTimeAction.setText(action_time[i]);

									TextView tvAction = new TextView(context);
									tvAction.setLayoutParams(layoutparams);
									tvAction.setText(actions[i]);

									TextView tvAuthority = new TextView(context);
									tvAuthority.setLayoutParams(layoutparams);
									tvAuthority
											.setText("by- " + authorities[i]);

									layoutparams.setMargins(10, 10, 10, 30);
									llActionHolder
											.setLayoutParams(layoutparams);
									llActionHolder.addView(tvTimeAction);
									llActionHolder.addView(tvAction);
									llActionHolder.addView(tvAuthority);

									llActions.addView(llActionHolder);
								}
								if (n == 0) {
									TextView tv = new TextView(context);
									LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
											LayoutParams.MATCH_PARENT,
											LayoutParams.WRAP_CONTENT);
									layoutparams.setMargins(10, 30, 10, 30);
									tv.setLayoutParams(layoutparams);
									tv.setText("No Actions Taken Yet");
									llActions.addView(tv);
								}
								progress.dismiss();

							} catch (JSONException e) {
								// TODO Auto-generated catch block
								Toast.makeText(context,
										"exception: " + e.getMessage(),
										Toast.LENGTH_LONG).show();
								progress.dismiss();
							}
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							Toast.makeText(context,
									"OnFailure: " + arg3.getMessage(),
									Toast.LENGTH_LONG).show();
							progress.dismiss();
						}
					});
		} catch (Exception e) {
			Toast.makeText(context, "exception: " + e.getMessage(),
					Toast.LENGTH_LONG).show();
			progress.dismiss();
		}
	}

	protected void editComment(int comm_id, String newCome) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("comment_id", "" + comm_id);
		params.add("comment", newCome);
		try {
			client.post(Utility.getURL(ComplainSingle.this) + "EditComment",
					params, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							String response = new String(arg2);
							Toast.makeText(context, "" + response,
									Toast.LENGTH_LONG).show();
							progress.dismiss();
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							progress.dismiss();
						}
					});
		} catch (Exception e) {
			progress.dismiss();
		}

	}

	protected void deleteComment(int comment_id) {
		AsyncHttpClient client = new AsyncHttpClient();
		RequestParams params = new RequestParams();
		params.add("comment_id", "" + comment_id);
		try {
			client.post(Utility.getURL(ComplainSingle.this) + "DeleteComment",
					params, new AsyncHttpResponseHandler() {

						@Override
						public void onSuccess(int arg0, Header[] arg1,
								byte[] arg2) {
							String response = new String(arg2);
							Toast.makeText(context, "" + response,
									Toast.LENGTH_LONG).show();
							progress.dismiss();
						}

						@Override
						public void onFailure(int arg0, Header[] arg1,
								byte[] arg2, Throwable arg3) {
							// TODO Auto-generated method stub
							progress.dismiss();
						}
					});
		} catch (Exception e) {
			progress.dismiss();
		}

	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.bPostComment_ComplaintSingle) {
			String comment = etComment.getText().toString();
			if (comment.length() == 0) {
				Toast.makeText(context, "Please Enter a Comment!",
						Toast.LENGTH_SHORT).show();
			} else {
				progress = ProgressDialog.show(ComplainSingle.this,
						"Please Wait", "Uploading Your Comment");
				AsyncHttpClient client = new AsyncHttpClient();
				RequestParams params = new RequestParams();
				params.put("user_id", user_id);
				params.put("complaint_id", id);
				params.put("body", comment);
				try {
					client.post(Utility.getURL(this) + "PostComment", params,
							new AsyncHttpResponseHandler() {

								@Override
								public void onSuccess(int arg0, Header[] arg1,
										byte[] arg2) {
									// TODO Auto-generated method stub
									String response = new String(arg2);
									StringTokenizer st1 = new StringTokenizer(
											response, "##$$##");
									String username = st1.nextToken();
									String time = st1.nextToken();
									final String body = st1.nextToken();
									final String comm_id = st1.nextToken();
									n++;
									LinearLayout.LayoutParams layoutparams = new LinearLayout.LayoutParams(
											800, LayoutParams.WRAP_CONTENT);
									layoutparams.setMargins(10, 10, 10, 10);
									LinearLayout singleCommentContainer = new LinearLayout(
											context);
									singleCommentContainer
											.setOrientation(LinearLayout.VERTICAL);

									LinearLayout ll3 = new LinearLayout(context);
									ll3.setLayoutParams(layoutparams);
									ll3.setPadding(5, 5, 5, 5);
									ll3.setOrientation(LinearLayout.VERTICAL);

									// Sender
									TextView tvSender = new TextView(context);
									tvSender.setText(username);
									tvSender.setTypeface(null, Typeface.BOLD);
									tvSender.setPadding(10, 10, 10, 10);

									// Time
									TextView tvTime = new TextView(context);
									tvTime.setText(time);
									tvTime.setTypeface(null, Typeface.ITALIC);
									tvTime.setPadding(0, 0, 10, 0);
									tvTime.setGravity(Gravity.RIGHT);

									// comment body
									final TextView tvComment = new TextView(
											context);
									tvComment.setText(body);
									tvComment.setLayoutParams(layoutparams);
									tvComment.setPadding(10, 10, 10, 10);
									final int iF = ll1.getChildCount();
									// Edit Button
									ImageButton ibEdit = new ImageButton(
											context);
									ibEdit.setImageResource(R.drawable.edit);
									ibEdit.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									ibEdit.setId(iF);
									ibEdit.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											LayoutInflater li = LayoutInflater
													.from(context);
											View promptsView = li
													.inflate(
															R.layout.layout_edit_comment,
															null);

											AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
													context);

											// set prompts.xml to alert dialog
											// builder
											alertDialogBuilder
													.setView(promptsView);

											final EditText userInput = (EditText) promptsView
													.findViewById(R.id.editTextDialogUserInput);
											userInput.setText(tvComment
													.getText().toString());
											// set dialog message
											alertDialogBuilder
													.setCancelable(false)
													.setPositiveButton(
															"Confirm",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {

																	String newCom = userInput
																			.getText()
																			.toString();
																	if (newCom
																			.length() == 0) {
																		Toast.makeText(
																				context,
																				"Please enter a comment",
																				Toast.LENGTH_SHORT)
																				.show();
																	} else {
																		tvComment
																				.setText(newCom);

																		editComment(
																				Integer.parseInt(comm_id),
																				newCom);
																		progress = ProgressDialog
																				.show(ComplainSingle.this,
																						"Wait",
																						"Updating Comment...");
																	}
																}

															})
													.setNegativeButton(
															"Cancel",
															new DialogInterface.OnClickListener() {
																public void onClick(
																		DialogInterface dialog,
																		int id) {
																	dialog.cancel();
																}
															});

											AlertDialog alertDialog = alertDialogBuilder
													.create();
											alertDialog.show();
										}
									});

									// Delete Button
									ImageButton ibDelete = new ImageButton(
											context);
									ibDelete.setImageResource(R.drawable.action_delete);
									ibDelete.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									ibDelete.setOnClickListener(new OnClickListener() {

										@Override
										public void onClick(View v) {
											AlertDialog.Builder builder = new AlertDialog.Builder(
													ComplainSingle.this);
											// Add the buttons
											builder.setPositiveButton(
													"Confirm ",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id1) {
															// User clicked OK
															// button
															progress = ProgressDialog
																	.show(ComplainSingle.this,
																			"Wait",
																			"Deleting...");
															deleteComment(Integer
																	.parseInt(comm_id));
															ll1.removeViewAt(iF);
														}
													});
											builder.setNegativeButton(
													"Cancel",
													new DialogInterface.OnClickListener() {
														public void onClick(
																DialogInterface dialog,
																int id) {
															// User cancelled
															// the
															// dialog
														}
													});
											// Set other dialog properties
											builder.setTitle("Confirm??");
											builder.setMessage("Are you sure you want to delete this comment??");
											builder.setIcon(R.drawable.ic_action_help);
											builder.show();

										}
									});

									// Top Line Holder
									LinearLayout ll4 = new LinearLayout(context);
									ll4.setLayoutParams(layoutparams);
									singleCommentContainer
											.setGravity(Gravity.RIGHT);
									ll4.addView(ibEdit);
									ll4.addView(ibDelete);

									ll4.setGravity(Gravity.RIGHT);
									ll4.setOrientation(LinearLayout.HORIZONTAL);
									ll4.setPadding(0, 0, 13, 0);
									RelativeLayout rl = new RelativeLayout(
											context);
									rl.setLayoutParams(new LinearLayout.LayoutParams(
											LayoutParams.WRAP_CONTENT,
											LayoutParams.WRAP_CONTENT));
									rl.addView(tvSender);
									rl.addView(ll4);

									ll3.addView(rl);
									ll3.addView(tvComment);
									ll3.addView(tvTime);
									ll3.setBackgroundResource(R.drawable.comment_layout);

									// add components to singleCommentContainer

									singleCommentContainer.setPadding(5, 5, 5,
											5);
									singleCommentContainer.addView(ll3);
									ll1.addView(singleCommentContainer);
									etComment.setText("");
									progress.dismiss();
								}

								@Override
								public void onFailure(int arg0, Header[] arg1,
										byte[] arg2, Throwable arg3) {
									Toast.makeText(
											context,
											"Error in connection comment:"
													+ arg3.getMessage(),
											Toast.LENGTH_LONG).show();
									progress.dismiss();

								}
							});
				} catch (Exception e) {
					Toast.makeText(context,
							"Error in posting comment:" + e.getMessage(),
							Toast.LENGTH_LONG).show();
					progress.dismiss();
				}
			}
		} else {
			Toast.makeText(context, v.getId() + "", Toast.LENGTH_SHORT).show();
		}

	}
}
