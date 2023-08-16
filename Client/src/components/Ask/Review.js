import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown, faChevronUp } from '@fortawesome/free-solid-svg-icons';
import { useState } from 'react';
import { useSelector } from 'react-redux';
import ReviewBtn from './button/ReviewBtn';
import TipReview from './Tips/TipReview';

export default function Review({ isSelected, setisSelected }) {
  const [isOpen, setisopen] = useState(false);

  const tipboxNum = useSelector((state) => {
    return state.tipbox.value; // store 안에 reducer가 저장되어 있는 Slice의 이름
  });

  return (
    <div className="relative flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
      <div className="font-semibold">
        Review questions already on Stack Overflow to see if your question is a
        duplicate.
      </div>

      <div className="my-1 text-xs">
        Clicking on these questions will open them in a new tab for you to
        review. Your progress here will be saved so you can come back and
        continue.
      </div>

      <div
        className="flex flex-row justify-between items-center mt-4 py-2 px-2 text-base text-gray-500 bg-gray-100 border-2 border-solid border-gray rounded-t-md active:border-black"
        onClick={() => {
          setisopen(!isOpen);
          setisSelected(5);
        }}
      >
        <div>Do any of these posts answer your question?</div>
        {!isOpen ? (
          <FontAwesomeIcon icon={faChevronDown} className="mr-2" />
        ) : (
          <FontAwesomeIcon icon={faChevronUp} className="mr-2" />
        )}
      </div>
      {!isOpen || (
        <div className="py-3 bg-white text-center text-sm text-gray-500 border-l-2 border-r-2 border-b-2 border-solid border-gray rounded-b-md">
          No duplicate questions found.
        </div>
      )}

      {tipboxNum === 5 ? <ReviewBtn /> : null}
      {isSelected === 5 && <TipReview />}
    </div>
  );
}
