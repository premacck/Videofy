package com.cncoding.teazer.data.api.calls.authentication;

import com.cncoding.teazer.data.api.ResultObject;
import com.cncoding.teazer.model.base.Authorize;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Prem$ on 2/5/2018.
 **/

public interface AuthenticationService {

    /**
     * Call this service for Signup into application
     * Signup Step 1.
     * @return :
     *      200: If status is true OTP will send to given mobile number.
     *           If Status is false the Username, Email and Phone Number already exist or you may reached maximum OTP retry attempts.
     *   or 400: Invalid JSON Format present in Request Body
     *   or 412: Validation Failed
     * @param signUpBody containing username, first name, last name, email, password, phone number and country code.
     */
    @POST("/api/v1/authentication/signup")
    Call<ResultObject> signUp(@Body Authorize signUpBody);

    /**
     * Call this service for verify the OTP and complete the signup process.
     * Signup Step 1.
     * @return :
     *      200: If status is true OTP will send to given mobile number.
     *           If Status is false the Username, Email and Phone Number already exist or you may reached maximum OTP retry attempts.
     *   or 400: Invalid JSON Format present in Request Body
     *   or 412: Validation Failed
     * @param verifySignUp containing fcm_token, device_id, device_type, username, first name, last name,
     *                     email, password, phone number, country code and otp.
     */
    @POST("/api/v1/authentication/signup/verify")
    Call<ResultObject> verifySignUp(@Body Authorize verifySignUp);

    @POST("/api/v1/authentication/social/signup")
    Call<ResultObject> socialSignUp(@Body Authorize socialSignUpDetails);

    /**
     * Perform sign in with password.
     */
    @POST("/api/v1/authentication/signin/with/password")
    Call<ResultObject> loginWithPassword(@Body Authorize loginWithPassword);

    /**
     * Perform sign in with OTP.
     * Login step 1.
     */
    @POST("/api/v1/authentication/signin/with/otp")
    Call<ResultObject> loginWithOtp(@Body Authorize phoneNumberDetails);

    /**
     * Verify sign in with OTP.
     * Login step 2.
     */
    @POST("/api/v1/authentication/signin/with/otp/verify")
    Call<ResultObject> verifyLoginWithOtp(@Body Authorize verifyLoginWithOtp);

    /**
     * Check username availability, Pass in @param String username
     * */
    @GET("/api/v1/authentication/check/username/{username}")
    Call<ResultObject> checkUsernameAvailability(@Path("username") String username);

    /**
     * Check email availability, Pass in @param String email
     * */
    @GET("/api/v1/authentication/check/email/{email}")
    Call<ResultObject> checkEmailAvailability(@Path("email") String email);

    /**
     * Check phone number availability, Pass in @param String phoneNumber
     * */
    @GET("/api/v1/authentication/check/phonenumber")
    Call<ResultObject> checkPhoneNumberAvailability(
            @Query("countryCode") int countryCode, @Query("phonenumber") long phoneNumber);

    /**
     * To verify the forgot password OTP
     * */
    @GET("/api/v1/authentication/reset/password/verify/otp/{otp}")
    Call<ResultObject> verifyForgotPasswordOtp(@Path("otp") int otp);

    /**
     * Forgot password reset by email
     * Call this service to get OTP for reset the password from forgot password
     * */
    @POST("/api/v1/authentication/reset/password/by/{email_id}")
    Call<ResultObject> requestResetPasswordByEmail(@Path("email_id") String email);

    /**
     * Reset password by OTP
     * Call this service to reset the password by OTP received in email or phone number
     */
    @POST("/api/v1/authentication/reset/password/by/phonenumber")
    Call<ResultObject> requestResetPasswordByPhone(@Body Authorize phoneNumberDetails);

    /**
     * Call this service to reset the password by OTP received in email or phone number.
     * Either you need to send Email or Phone number and Country code along with OTP and New password
     * */
    @POST("/api/v1/authentication/password/reset")
    Call<ResultObject> resetPasswordByOtp(@Body Authorize resetPasswordDetails);
}