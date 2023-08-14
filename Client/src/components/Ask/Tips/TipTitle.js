export default function TipTitle() {
  return (
    <div className="absolute top-0 left-216 flex flex-col justify-center items-center border border-solid border-gray-300 rounded-md shadow-xss">
      <div className="w-84 h-11 py-2 pl-3 bg-gray-100 border border-solid border-b-gray-300 rounded-t-md">
        Writing a good title
      </div>
      <div className="flex flex-row justify-center items-center w-84 h-32 bg-white rounded-b-md">
        <img className="w-12 h-14 mx-5" src="../Draw.png" />
        <div className="mr-5 w-132">
          <div className="py-2 text-xs">
            Your title should summarize the problem.
          </div>
          <div className="py-2 text-xs">
            You might find that you have a better idea of your title after
            writing out the rest of the question.
          </div>
        </div>
      </div>
    </div>
  );
}
