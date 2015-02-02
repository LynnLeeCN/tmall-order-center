package com.github.lynnleecn.tmall.servlet.auth;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.lynnleecn.tmall.config.ConnectionConfig;
import com.github.lynnleecn.tmall.config.ConnectionUtil;
import com.github.lynnleecn.tmall.config.constant.Constants;
import com.taobao.api.internal.util.WebUtils;

/**
 * Servlet implementation class OAuthCallbackServlet
 */
@WebServlet(urlPatterns = "/auth/callback", loadOnStartup = 5)
public class OAuthCallbackServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String OAUTHED = "oAuthed";

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		// Authorization state
		String state = request.getParameter("state");
		// Domain
		String basePath = ConnectionConfig.appServer + request.getContextPath();
		// TaoBao callback address
		String callbackPath = basePath + "/auth/callback";

		// Get token after user click authorize
		if (state != null && state.equals(OAUTHED)) {

			String code = request.getParameter("code");

			String error = request.getParameter("error");
			String error_description = request.getParameter("error_description");

			if (code == null && error != null && error_description != null) {
				request.setAttribute("error", error);
				request.setAttribute("errorDescription", error_description);
			} else {
				Map<String, String> props = new HashMap<String, String>();

				// oAuth page layout(tmall or web)
				props.put("view", "tmall");
				props.put("grant_type", "authorization_code");

				props.put("client_id", ConnectionConfig.appKey);
				props.put("client_secret", ConnectionConfig.appSecret);

				props.put("code", code);
				props.put("redirect_uri", callbackPath);

				// TaoBaoUtil: get token
				String tokenResp = WebUtils.doPost(Constants.TOKEN_URL, props, 30000, 30000);

				JSONObject token;
				String accessToken;
				try {

					// format to JSON object
					token = new JSONObject(tokenResp);
					accessToken = token.getString("access_token");
					// store access token to utility
					ConnectionUtil.setToken(token.getString("taobao_user_id"), accessToken);

					// jump to token.jsp
					request.setAttribute("userId", token.getString("taobao_user_id"));
					request.setAttribute("nickName", token.getString("taobao_user_nick"));
					request.setAttribute("tokenType", token.getString("token_type"));
					request.setAttribute("accessToken", token.getString("access_token"));
					request.setAttribute("refreshToken", token.getString("refresh_token"));
					request.getRequestDispatcher("/test/token.jsp").forward(request, response);

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// jump to TaoBao OAuth page
		} else {
			response.sendRedirect(response.encodeRedirectURL(Constants.OAUTH_URL + "?response_type=code&client_id="
					+ ConnectionConfig.appKey + "&redirect_uri=" + callbackPath + "&state=" + OAUTHED + "&view=tmall"));
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log("Call post method!");
	}

}
