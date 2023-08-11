import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import {
  faMedal,
  faSort,
  faComments,
  faBookmark,
} from '@fortawesome/free-solid-svg-icons';

export default function SignUpInfo() {
  return (
    <div className="flex flex-col justify-center items-start w-124 h-220 mr-3 pl-3">
      <div className="font-semibold text-2xl">Join the Stack Overflow community</div>
      <ul>
        <li className="flex flex-row justify-start items-center my-6 text-md">
          <FontAwesomeIcon
            icon={faComments}
            style={{ color: '#0B96FF' }}
            className="w-7 h-7 mr-3"
          />
          Get unstuck — ask a question
        </li>
        <li className="flex flex-row justify-start items-center my-6 text-md">
          <FontAwesomeIcon
            icon={faSort}
            style={{ color: '#0B96FF' }}
            className="w-7 h-7 mr-3"
          />
          Unlock new privileges like voting and commenting
        </li>
        <li className="flex flex-row justify-start items-center my-6 text-md">
          <FontAwesomeIcon
            icon={faBookmark}
            style={{ color: '#0B96FF' }}
            className="w-7 h-6 mr-3"
          />
          Save your favorite questions, answers, watch tags, and more
        </li>
        <li className="flex flex-row justify-start items-center my-6 text-md">
          <FontAwesomeIcon
            icon={faMedal}
            style={{ color: '#0B96FF' }}
            className="w-7 h-6 mr-3"
          />
          Earn reputation and badges
        </li>
      </ul>
      <div className="flex flex-col my-6">
        <span className="text-xs">
          Collaborate and share knowledge with a private group for FREE.
        </span>
        <span className="text-xs mt-1 text-sky-500 hover:text-sky-600 cursor-pointer">
          Get Stack Overflow for Teams free for up to 50 users.
        </span>
      </div>
    </div>
  );
}
