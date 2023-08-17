import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faGithub } from '@fortawesome/free-brands-svg-icons';
import { useNavigate, Link } from 'react-router-dom';
import { RouteConst } from '../../../Interface/RouteConst';
import {
  handleGoogleLogin,
  handleGithubLogin,
  handleKakaoLogin,
} from '../../../OAuth/OAuth';
import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import { google, github, kakao } from '../../../redux/createSlice/OAuthSlice';
import { email, password } from '../../../redux/createSlice/LoginInfoSlice';
import { errmsg } from '../../../redux/createSlice/ErrMsgSlice';
import { logintoken } from '../../../redux/createSlice/LoginInfoSlice';

export default function LoginForm() {
  const dispatch = useDispatch();
  const nevigate = useNavigate();
  const LoginInfo = useSelector((state) => state.logininfo.value);
  const ErrorMessage = useSelector((state) => state.errmsg.value);
  const handleLogin = () => {
    if (!LoginInfo.email || !LoginInfo.password) {
      return dispatch(errmsg('Please enter all information.'));
    }
    return axios
      .post(
        'http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/auth/login',
        LoginInfo
      )
      .then((res) => {
        console.log(res);
        dispatch(logintoken(res.headers.authorization));
        dispatch(errmsg(''));
        nevigate(RouteConst.Main);
      })
      .catch((err) => {
        dispatch(errmsg('Login is failed'));
      });
  };

  return (
    <div className="flex flex-col items-center">
      <img
        className="w-8 h-10 cursor-pointer"
        src="./../StackOverflow.png"
        alt="StackOverflow"
      />
      <ul className="flex flex-col items-center my-5">
        <li
          className="flex flex-row justify-center items-center w-70 h-10 my-1 bg-white hover:bg-gray-200 border border-solid border-gray rounded-md cursor-pointer"
          onClick={() => {
            dispatch(google);
            handleGoogleLogin();
          }}
        >
          <img
            src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"
            alt="Google"
            className="w-4 h-4 mr-2"
          />
          <span className="text-sm">Sign up with Google</span>
        </li>
        <li
          className="flex felx-row justify-center items-center w-70 h-10 my-1 bg-gray-800 hover:bg-gray-700 border border-solid border-gray text-white rounded-md cursor-pointer"
          onClick={() => {
            dispatch(github);
            handleGithubLogin();
          }}
        >
          <FontAwesomeIcon icon={faGithub} className="w-4 h-4 mr-2" />
          <span className="text-sm">Sign up with GitHub</span>
        </li>
        <li
          className="flex felx-row justify-center items-center w-70 h-10 my-1 bg-yellow-300 hover:bg-yellow-200 border border-solid border-gray rounded-md cursor-pointer"
          onClick={() => {
            dispatch(kakao);
            handleKakaoLogin();
          }}
        >
          <img
            src="https://upload.wikimedia.org/wikipedia/commons/e/e3/KakaoTalk_logo.svg"
            alt="KakaoTalk"
            className="w-5 h-5 mr-2"
          />
          <span className="text-sm">Sign up with Kakao</span>
        </li>
      </ul>

      <form className="flex flex-col justify-center items-center w-70 h-64 mt-1 bg-white border border-solid border-gray rounded-md shadow-xss">
        <div className="flex flex-col justify-center items-center">
          <span className="mt-2 w-58 text-left text-md font-semibold">
            Email
          </span>
          <input
            className="my-1 w-58 h-9 pl-2 border-2 border-solid border-gray rounded-md text-sm"
            type="text"
            onChange={(e) => dispatch(email(e.target.value))}
          ></input>
        </div>

        <div className="flex flex-col justify-center items-center">
          <div className="flex flex-row items-end w-58">
            <span className="mt-2 w-58 text-left text-md font-semibold">
              Password
            </span>
            <span className="mt-2 w-58 text-right text-xs text-sky-500 hover:text-sky-600 cursor-pointer">
              Forget password?
            </span>
          </div>
          <input
            className="my-1 w-58 h-9 pl-2 border-2 border-solid border-gray rounded-md text-sm"
            type="password"
            onChange={(e) => dispatch(password(e.target.value))}
          ></input>
        </div>

        <div className="mt-2 text-sm text-red-500">{ErrorMessage}</div>

        <button
          className="flex flex-col justify-center items-center w-58 h-9 my-3 bg-sky-500 hover:bg-sky-600 text-sm text-white text-center rounded-md"
          onClick={(e) => {
            e.preventDefault();
            handleLogin();
          }}
          type="submit"
        >
          Log in
        </button>
      </form>

      <div className="w-70 mt-8 text-sm text-center">
        Don’t have an account?{' '}
        <Link to={RouteConst.SignUp}>
          <span className=" text-sky-500 hover:text-sky-600 cursor-pointer">
            Sign up
          </span>{' '}
        </Link>
      </div>
      <div className="w-70 my-3 text-sm text-center mb-18">
        Are you an employer?{' '}
        <span className=" text-sky-500 hover:text-sky-600 cursor-pointer">
          Sign up on Talent
        </span>{' '}
      </div>
    </div>
  );
}
