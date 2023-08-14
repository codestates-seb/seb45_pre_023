import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBell } from '@fortawesome/free-regular-svg-icons';

export default function TipReview() {
  return (
    <div className="absolute top-0 left-216 flex flex-col justify-center items-center border border-solid border-gray-300 rounded-md shadow-xss">
      <div className="w-84 h-17 py-2 pl-3 bg-gray-100 border border-solid border-b-gray-300 rounded-t-md">
        Make sure we don’t already have an answer for your question
      </div>
      <div className="flex flex-row justify-center items-center w-84 h-34 bg-white rounded-b-md">
        <FontAwesomeIcon icon={faBell} className="w-12 h-24 mx-5" />
        <div className="mr-5 w-132">
          <div className="py-2 text-xs">
            Stack Overflow is a huge database of knowledge.
          </div>
          <div className="py-2 text-xs">
            Please make sure your question isn’t already answered before
            posting, or your question{' '}
            <span className="text-sky-600 hover:text-sky-500 cursor-pointer">
              might be closed as a duplicate.
            </span>
          </div>
        </div>
      </div>
    </div>
  );
}
