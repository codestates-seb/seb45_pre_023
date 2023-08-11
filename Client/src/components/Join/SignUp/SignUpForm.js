import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faGithub } from '@fortawesome/free-brands-svg-icons';

export default function SignUpForm() {
  return (
    <div className="flex flex-col items-center">
      <ul className="flex flex-col items-center mt-8">
        <li className="flex flex-row justify-center items-center w-80 h-10 my-1 bg-white hover:bg-gray-200 border border-solid border-gray rounded-md cursor-pointer">
          <img
            src="https://upload.wikimedia.org/wikipedia/commons/5/53/Google_%22G%22_Logo.svg"
            className="w-4 h-4 mr-2"
          />
          <span className="text-sm">Sign up with Google</span>
        </li>
        <li className="flex felx-row justify-center items-center w-80 h-10 my-1 bg-gray-800 hover:bg-gray-700 border border-solid border-gray text-white rounded-md cursor-pointer">
          <FontAwesomeIcon icon={faGithub} className="w-4 h-4 mr-2" />
          <span className="text-sm">Sign up with GitHub</span>
        </li>
        <li className="flex felx-row justify-center items-center w-80 h-10 my-1 bg-yellow-200 hover:bg-yellow-300 border border-solid border-gray rounded-md cursor-pointer">
          <img
            src="https://upload.wikimedia.org/wikipedia/commons/e/e3/KakaoTalk_logo.svg"
            className="w-5 h-5 mr-2"
          />
          <span className="text-sm">Sign up with Kakao</span>
        </li>
      </ul>

      <div className="flex flex-col justify-center items-center w-80 h-180 mt-3 bg-white border border-solid border-gray rounded-md shadow-xss">
        <div className="flex flex-col justify-center items-center">
          <span className="mt-2 w-68 text-left text-md font-semibold">
            Display name
          </span>
          <input
            className="my-1 w-68 h-9 pl-2 border-2 border-solid border-gray rounded-md text-xs"
            type="text"
          ></input>
        </div>

        <div className="flex flex-col justify-center items-center">
          <span className="mt-2 w-68 text-left text-md font-semibold">
            Email
          </span>
          <input
            className="my-1 w-68 h-9 pl-2 border-2 border-solid border-gray rounded-md text-xs"
            type="text"
          ></input>
        </div>

        <div className="flex flex-col justify-center items-center">
          <span className="mt-2 w-68 text-left text-md font-semibold">
            Password
          </span>
          <input
            className="my-1 w-68 h-9 pl-2 border-2 border-solid border-gray rounded-md text-xs"
            type="password"
          ></input>
          <span className="w-68 text-xs text-gray-400">
            Passwords must contain at least eight characters, including at least
            1 letter and 1 number.
          </span>
        </div>

        <div className="flex flex-col justify-center items-center w-68 h-44 my-3 bg-gray-200 rounded-sm">
          <div className="flex flex-col justify-start items-center w-40 h-36 bg-gray-100 rounded-sm shadow-xss">
            <div className="flex flex-row justify-center items-center my-8">
              <input className="w-7 h-7" type="checkbox"></input>
              <span className="ml-2 text-sm">I'm not a robot</span>
            </div>

            <div className="flex flex-row justify-center items-center text-xss mt-3">
              <img src="" />
              reCAPTCHA
            </div>

            <div className="flex flex-row justify-center items-center w-68 text-xss">
              Privacy ● Terms
            </div>
          </div>
        </div>

        <div className="flex flex-row justify-between items-start w-68 my-1">
          <input className="mt-1" type="checkbox"></input>
          <span className="ml-2 pr-4 text-xs">
            Opt-in to receive occasional product updates, user research
            invitations, company announcements, and digests.
          </span>
        </div>

        <div className="flex flex-col justify-center items-center w-68 h-9 my-3 bg-sky-500 hover:bg-sky-600 text-sm text-white text-center rounded-md">
          Sign up
        </div>

        <div className="w-66 mt-6 text-xs">
          By clicking “Sign up”, you agree to our{' '}
          <span className=" text-sky-500 hover:text-sky-600 cursor-pointer">
            terms of service
          </span>{' '}
          and acknowledge that you have read and understand our{' '}
          <span className=" text-sky-500 hover:text-sky-600 cursor-pointer">
            privacy policy
          </span>{' '}
          and
          <span className=" text-sky-500 hover:text-sky-600 cursor-pointer">
            code of conduct.
          </span>
        </div>
      </div>

      <div className="w-68 mt-8 text-sm text-center">
        Already have an account?{' '}
        <span className=" text-sky-500 hover:text-sky-600 cursor-pointer">
          Log in
        </span>{' '}
      </div>
      <div className="w-68 my-3 text-sm text-center mb-18">
        Are you an employer?{' '}
        <span className=" text-sky-500 hover:text-sky-600 cursor-pointer">
          Sign up on Talent
        </span>{' '}
      </div>
    </div>
  );
}
