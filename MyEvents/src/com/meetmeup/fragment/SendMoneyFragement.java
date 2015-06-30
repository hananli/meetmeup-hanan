package com.meetmeup.fragment;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.R;
import com.meetmeup.asynch.TransactionUpdateAsync;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.Utill;
import com.paypal.android.sdk.payments.PayPalAuthorization;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalFuturePaymentActivity;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalProfileSharingActivity;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;

//This class is used for sending money to the event owner.
public class SendMoneyFragement extends Fragment {
	public static EventsBean eventBean = null;
	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	EditText AmountTV;
	TextView eventTitle;
	Button PayBtn;
	static Activity mActivity;
	DashBoard dashBoard;

	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			mfFragment = new SendMoneyFragement();
		}
		return mfFragment;
	}

	

	@Override
	public void onStart() {
		if (DashBoard.actionBar != null) {
			DashBoard.resetActionBarTitle("Send Money",-2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
//			DashBoard.chatIcon.setVisibility(View.GONE);
		}
		AmountTV.setText(eventBean.getAmount());
		eventTitle.setText(eventBean.getEvent_title());
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.send_money_view, container, false);
		dashBoard = (DashBoard)mActivity;
		initializeView(view);
		setOnClickeListeners();
		return view;
	}

	void initializeView(View view) {
		AmountTV = (EditText) view.findViewById(R.id.amount);
		PayBtn = (Button) view.findViewById(R.id.pay);
		eventTitle =(TextView)view.findViewById(R.id.event_title);
	}

	public void setOnClickeListeners() {

		DashBoard.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFragmentManager.getBackStackEntryCount() > 0) {
					mFragmentManager.popBackStack();
				}
			}
		});
		PayBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utill.showToast(mContext, "Send Money");
				String amount = AmountTV.getText().toString();
				onBuyPressed(v,amount);
			}
		});
	}

	private static final String TAG = "paymentExample";
	/**
	 * - Set to PayPalConfiguration.ENVIRONMENT_PRODUCTION to move real money.
	 * 
	 * - Set to PayPalConfiguration.ENVIRONMENT_SANDBOX to use your test
	 * credentials from https://developer.paypal.com
	 * 
	 * - Set to PayPalConfiguration.ENVIRONMENT_NO_NETWORK to kick the tires
	 * without communicating to PayPal's servers.
	 */
	private static final String CONFIG_ENVIRONMENT = PayPalConfiguration.ENVIRONMENT_SANDBOX;

	// note that these credentials will differ between live & sandbox
	// environments.
	private static final String CONFIG_CLIENT_ID = "ATWNwTl_zfrTaPCqZp8riLewhn1tsJPIWxyzK-KXuZOIaAXpaUGnTQd66JhscSM3HoS92qR1An_cND0i";

	private static final int REQUEST_CODE_PAYMENT = 1;
	private static final int REQUEST_CODE_FUTURE_PAYMENT = 2;
	private static final int REQUEST_CODE_PROFILE_SHARING = 3;

	private void sendAuthorizationToServer(PayPalAuthorization authorization) {

		/**
		 * TODO: Send the authorization response to your server, where it can
		 * exchange the authorization code for OAuth access and refresh tokens.
		 * 
		 * Your server must then store these tokens, so that your server code
		 * can execute payments for this user in the future.
		 * 
		 * A more complete example that includes the required app-server to
		 * PayPal-server integration is available from
		 * https://github.com/paypal/
		 * rest-api-sdk-python/tree/master/samples/mobile_backend
		 */

	}

	public void onBuyPressed(View pressed,String amount) {
		Double d = Double.parseDouble(amount);
		PayPalPayment thingToBuy = new PayPalPayment(new BigDecimal(d),"USD",""+eventBean.getEvent_title());
		
		
		
		
		
		

		Intent intent = new Intent(mActivity, PaymentActivity.class);
		intent.putExtra(PaymentActivity.EXTRA_PAYPAL_ENVIRONMENT, CONFIG_ENVIRONMENT);
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
		intent.putExtra(PaymentActivity.EXTRA_RECEIVER_EMAIL,eventBean.getPaypalId());
		// It's important to repeat the clientId here so that the SDK has it if
		// Android restarts your
		// app midway through the payment UI flow.
		intent.putExtra(PaymentActivity.EXTRA_CLIENT_ID, CONFIG_CLIENT_ID);
		// intent.putExtra(PaymentActivity.EXTRA_PAYER_ID,
		// "your-customer-id-in-your-system");
		intent.putExtra(PaymentActivity.EXTRA_PAYMENT, thingToBuy);
		
		//for disable creadit card.
		 intent.putExtra(PaymentActivity.EXTRA_SKIP_CREDIT_CARD, true); 
		
		

		startActivityForResult(intent, 0);

		// System.out.println("NEW TEST... onBuyPressed() in GoTaskernotificationItemActivity");
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
//TODO make it ==
		if (requestCode == 0) {
			if (resultCode == Activity.RESULT_OK) {
				PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
				if (confirm != null) {
					try {
						Log.i(TAG, confirm.toJSONObject().toString(4));
						Log.i(TAG, confirm.getPayment().toJSONObject().toString(4));
						Toast.makeText(mContext, "PaymentConfirmation info received from PayPal", Toast.LENGTH_LONG).show();

						JSONObject jObj = confirm.toJSONObject();
						/*{
						    "payment": {
						        "short_description": "very cool",
						        "amount": "100",
						        "currency_code": "USD"
						    },
						    "client": {
						        "platform": "Android",
						        "paypal_sdk_version": "1.2.5",
						        "product_name": "PayPal Android SDK; ",
						        "environment": "sandbox"
						    },
						    "proof_of_payment": {
						        "adaptive_payment": {
						            "timestamp": "2015-02-13T13:21:47+0000",
						            "payment_exec_status": "COMPLETED",
						            "app_id": "APP-80W284485P519543T",
						            "pay_key": "AP-9HV6790500006505C"
						        }
						    }
						}*/
						
						try {
							JSONObject paymentJson = jObj.getJSONObject("payment");
							JSONObject clientJson = jObj.getJSONObject("client");
							JSONObject proofOfPaymentJson = jObj.getJSONObject("proof_of_payment");
							// in case of credit card response is    {"rest_api":{"state":"approved","payment_id":"PAY-5GE548598W3261003KTQ7UDY"}}
							
							if(proofOfPaymentJson.has("rest_api")){
								JSONObject creaditResponse = proofOfPaymentJson.getJSONObject("rest_api");
								String t_id = creaditResponse.getString("payment_id");
								String amount = paymentJson.getString("amount");
								UserBean user = Utill.getUserPreferance(mContext);
							//	String status = creaditResponse.getString("payment_exec_status");
								
								MultipartEntity multi = new MultipartEntity();
								multi.addPart("event_id",new StringBody(eventBean.getEvent_id()));
								multi.addPart("user_id",new StringBody(user.getUser_id()));
								multi.addPart("amount",new StringBody(amount));
								multi.addPart("transaction_id",new StringBody(t_id));
								
								multi.addPart("transaction_status",new StringBody("1"));
								new TransactionUpdateAsync(mContext, new TransactionUpdateListener(), multi).execute();
							}
							else{
							JSONObject adaptivePayment = proofOfPaymentJson.getJSONObject("adaptive_payment");
							String t_id = adaptivePayment.getString("pay_key");
							String amount = paymentJson.getString("amount");
							UserBean user = Utill.getUserPreferance(mContext);
							String status = adaptivePayment.getString("payment_exec_status");
							
							MultipartEntity multi = new MultipartEntity();
							multi.addPart("event_id",new StringBody(eventBean.getEvent_id()));
							multi.addPart("user_id",new StringBody(user.getUser_id()));
							multi.addPart("amount",new StringBody(amount));
							multi.addPart("transaction_id",new StringBody(t_id));
							
							multi.addPart("transaction_status",new StringBody("1"));
							new TransactionUpdateAsync(mContext, new TransactionUpdateListener(), multi).execute();
							}
						} catch (UnsupportedEncodingException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
						
						
						
					} catch (JSONException e) {
						Log.e(TAG, "an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i(TAG, "The user canceled.");
			} else if (resultCode == PaymentActivity.RESULT_PAYMENT_INVALID) {
				Log.i(TAG, "An invalid Payment or PayPalConfiguration was submitted. Please see the docs.");
			}
 		} else if (requestCode == REQUEST_CODE_FUTURE_PAYMENT) {
			if (resultCode == Activity.RESULT_OK) {
				PayPalAuthorization auth = data.getParcelableExtra(PayPalFuturePaymentActivity.EXTRA_RESULT_AUTHORIZATION);
				if (auth != null) {
					try {
						Log.i("FuturePaymentExample", auth.toJSONObject().toString(4));

						String authorization_code = auth.getAuthorizationCode();
						Log.i("FuturePaymentExample", authorization_code);

						sendAuthorizationToServer(auth);
						Toast.makeText(mContext, "Future Payment code received from PayPal", Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e("FuturePaymentExample", "an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("FuturePaymentExample", "The user canceled.");
			} else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i("FuturePaymentExample",
						"Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
			}
		} else if (requestCode == REQUEST_CODE_PROFILE_SHARING) {
			if (resultCode == Activity.RESULT_OK) {
				PayPalAuthorization auth = data.getParcelableExtra(PayPalProfileSharingActivity.EXTRA_RESULT_AUTHORIZATION);
				if (auth != null) {
					try {
						Log.i("ProfileSharingExample", auth.toJSONObject().toString(4));

						String authorization_code = auth.getAuthorizationCode();
						Log.i("ProfileSharingExample", authorization_code);

						sendAuthorizationToServer(auth);
						Toast.makeText(mContext, "Profile Sharing code received from PayPal", Toast.LENGTH_LONG).show();

					} catch (JSONException e) {
						Log.e("ProfileSharingExample", "an extremely unlikely failure occurred: ", e);
					}
				}
			} else if (resultCode == Activity.RESULT_CANCELED) {
				Log.i("ProfileSharingExample", "The user canceled.");
			} else if (resultCode == PayPalFuturePaymentActivity.RESULT_EXTRAS_INVALID) {
				Log.i("ProfileSharingExample",
						"Probably the attempt to previously start the PayPalService had an invalid PayPalConfiguration. Please see the docs.");
			}
		}

		super.onActivityResult(requestCode, resultCode, data);
	}
	public class TransactionUpdateListener{
		public void onSuccess(String msg){
			Utill.showToast(mContext,"Amount sent successfully.");
			hideProgress();
			if (mFragmentManager.getBackStackEntryCount() > 0) {
				mFragmentManager.popBackStack();
			}
		}
		public void onError(String msg){
			Utill.showToast(mContext,"Error in transaction.");
			hideProgress();
		}
	}
	
	// This method is used for initializing activity instance
	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}

	ProgressDialog progress;

	// This method is used for showing progress bar.
	public void showProgress() {
		try {
			if (progress == null)
				progress = new ProgressDialog(mActivity);
			progress.setMessage("Please Wait..");
			progress.setCancelable(false);
			progress.show();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				progress = new ProgressDialog(mActivity);
				progress.setMessage("Please Wait..");
				progress.setCancelable(false);
				progress.show();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// This method is used for hiding progress bar
	public void hideProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}
	


}
