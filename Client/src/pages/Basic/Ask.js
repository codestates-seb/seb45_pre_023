import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faChevronDown, faChevronUp } from '@fortawesome/free-solid-svg-icons';
import { useState } from 'react';

export default function Ask() {
  const [isNum, setisNum] = useState(1);

  return (
    <div className="flex flex-col w-screen pl-84 bg-gray-100">
      <div className="my-11 font-semibold text-2xl">Ask a public question</div>

      <div className="mt-5 mb-2 p-7 w-212 bg-sky-50 border border-solid border-sky-300 rounded-md">
        <div className="pb-2 text-xl">Writing a good question</div>

        <div className="text-based">
          You’re ready to{' '}
          <span className="text-sky-600 hover:text-sky-500 cursor-pointer">
            ask
          </span>{' '}
          a{' '}
          <span className="text-sky-600 hover:text-sky-500 cursor-pointer">
            programming-related question
          </span>{' '}
          and this form will help guide you through the process.
        </div>

        <div className="text-based">
          Looking to ask a non-programming question? See{' '}
          <span className="text-sky-600 hover:text-sky-500 cursor-pointer">
            the topics here
          </span>{' '}
          to find a relevant site.
        </div>

        <ul className="mt-4 list-disc text-smm font-semibold">
          Steps
          <li className="ml-7 mt-2 font-normal text-smm">
            Summarize your problem in a one-line title.
          </li>
          <li className="ml-7 font-normal text-smm">
            Describe your problem in more detail.
          </li>
          <li className="ml-7 font-normal text-smm">
            Describe what you tried and what you expected to happen.
          </li>
          <li className="ml-7 font-normal text-smm">
            Add “tags” which help surface your question to members of the
            community.
          </li>
          <li className="ml-7 font-normal text-smm">
            Review your question and post it to the site.
          </li>
        </ul>
      </div>

      <div className="flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
        <div className="font-semibold">Title</div>

        <div className="my-1 text-xs">
          Be specific and imagine you’re asking a question to another person.
        </div>

        <input
          className="my-1 py-1.5 pl-2 text-sm bg-white border-2 border-solid border-gray rounded-md"
          placeholder="e.g. Is there an R function for finding the index of an element in a vector?"
        ></input>

        {isNum === 1 ? (
          <button
            className="w-12 h-9 mt-1 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
            onClick={() => {
              setisNum(isNum + 1);
            }}
          >
            Next
          </button>
        ) : null}
      </div>

      <div className="flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
        <div className="font-semibold">
          What are the details of your problem?
        </div>

        <div className="my-1 text-xs">
          Introduce the problem and expand on what you put in the title. Minimum
          20 characters.
        </div>

        <textarea className="my-1 py-1.5 pl-2 h-60 text-sm bg-white border-2 border-solid border-gray rounded-md"></textarea>

        {isNum === 2 ? (
          <button
            className="w-12 h-9 mt-1 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
            onClick={() => {
              setisNum(isNum + 1);
            }}
          >
            Next
          </button>
        ) : null}
      </div>

      <div className="flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
        <div className="font-semibold">
          What did you try and what were you expecting?
        </div>

        <div className="my-1 text-xs">
          Describe what you tried, what you expected to happen, and what
          actually resulted. Minimum 20 characters.
        </div>

        <textarea className="my-1 py-1.5 pl-2 h-60 text-sm bg-white border-2 border-solid border-gray rounded-md"></textarea>

        {isNum === 3 ? (
          <button
            className="w-12 h-9 mt-1 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
            onClick={() => {
              setisNum(isNum + 1);
            }}
          >
            Next
          </button>
        ) : null}
      </div>

      <div className="flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
        <div className="font-semibold">Tags</div>

        <div className="my-1 text-xs">
          Add up to 5 tags to describe what your question is about. Start typing
          to see suggestions.
        </div>

        <input
          className="mt-1 mb-3 py-1.5 pl-2 text-sm bg-white border-2 border-solid border-gray rounded-md"
          placeholder="e.g. (mysql json typescript)"
        ></input>

        {isNum === 4 ? (
          <button
            className="w-12 h-9 mt-1 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
            onClick={() => {
              setisNum(isNum + 1);
            }}
          >
            Next
          </button>
        ) : null}
      </div>

      <div className="flex flex-col my-2 px-6 py-5 w-212 bg-white border-2 border-solid border-gray rounded-md">
        <div className="font-semibold">
          Review questions already on Stack Overflow to see if your question is
          a duplicate.
        </div>

        <div className="my-1 text-xs">
          Clicking on these questions will open them in a new tab for you to
          review. Your progress here will be saved so you can come back and
          continue.
        </div>

        <div className="flex flex-row justify-between items-center my-4 py-2 px-2 text-base text-gray-500 bg-gray-100 border-2 border-solid border-gray rounded-md">
          <div>Do any of these posts answer your question?</div>
          <FontAwesomeIcon icon={faChevronDown} className="mr-2" />
        </div>

        <div className="flex flex-row justify-between items-center mt-4 py-2 px-2 text-base text-gray-500 bg-gray-100 border-2 border-solid border-gray rounded-t-md">
          <div>Do any of these posts answer your question?</div>
          <FontAwesomeIcon icon={faChevronUp} className="mr-2" />
        </div>
        <div className="mb-4 py-3 bg-white text-center text-sm text-gray-500 border-l-2 border-r-2 border-b-2 border-solid border-gray rounded-b-md">
          No duplicate questions found.
        </div>

        {isNum === 5 ? (
          <button
            className="w-34 h-9 mt-1 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
            onClick={() => {
              setisNum(isNum + 1);
            }}
          >
            Review your question
          </button>
        ) : null}
      </div>
      <button className="w-28 h-9 mt-1 mb-20 bg-gray-100 hover:bg-red-100 rounded-md text-xs text-red-600">
        Discard draft
      </button>
    </div>
  );
}
