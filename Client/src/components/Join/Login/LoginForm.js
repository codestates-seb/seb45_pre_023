import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faGithub } from '@fortawesome/free-brands-svg-icons';

export default function LoginForm() {
  return (
    <div className="flex flex-col items-center">
      <img className="w-9 h-12 cursor-pointer" src="./../StackOverflow.png" />
      <ul className="flex flex-col items-center mt-3">
        <li className="flex flex-row justify-center items-center w-70 h-10 my-1 bg-white hover:bg-gray-200 border border-solid border-gray rounded-md cursor-pointer">
          <img
            src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"
            className="w-4 h-4 mr-2"
          />
          <span className="text-sm">Sign up with Google</span>
        </li>
        <li className="flex felx-row justify-center items-center w-70 h-10 my-1 bg-gray-800 hover:bg-gray-700 border border-solid border-gray text-white rounded-md cursor-pointer">
          <FontAwesomeIcon icon={faGithub} className="w-4 h-4 mr-2" />
          <span className="text-sm">Sign up with GitHub</span>
        </li>
        <li className="flex felx-row justify-center items-center w-70 h-10 my-1 bg-yellow-300 hover:bg-yellow-200 border border-solid border-gray rounded-md cursor-pointer">
          <img
            src="https://upload.wikimedia.org/wikipedia/commons/e/e3/KakaoTalk_logo.svg"
            className="w-5 h-5 mr-2"
          />
          <span className="text-sm">Sign up with Kakao</span>
        </li>
      </ul>

      <div className="flex flex-col justify-center items-center w-70 h-60 mt-3 bg-white border border-solid border-gray rounded-md shadow-xss">
        <div className="flex flex-col justify-center items-center">
          <span className="mt-2 w-58 text-left text-md font-semibold">
            Email
          </span>
          <input
            className="my-1 w-58 h-9 pl-2 border-2 border-solid border-gray rounded-md text-xs"
            type="text"
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
            className="my-1 w-58 h-9 pl-2 border-2 border-solid border-gray rounded-md text-xs"
            type="password"
          ></input>
        </div>

        <div className="flex flex-col justify-center items-center w-58 h-9 my-3 bg-sky-500 hover:bg-sky-600 text-sm text-white text-center rounded-md">
          Log in
        </div>
      </div>

      <div className="w-70 mt-8 text-sm text-center">
        Don’t have an account?{' '}
        <span className=" text-sky-500 hover:text-sky-600 cursor-pointer">
          Sign up
        </span>{' '}
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
