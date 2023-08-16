export default function Discription() {
  return (
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
  );
}
