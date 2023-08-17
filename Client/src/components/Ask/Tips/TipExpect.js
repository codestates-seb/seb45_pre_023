export default function TipExpect() {
  return (
    <div className="absolute top-0 left-216 flex flex-col justify-center items-center border border-solid border-gray-300 rounded-md shadow-xss">
      <div className="w-84 h-11 py-2 pl-3 bg-gray-100 border border-solid border-b-gray-300 rounded-t-md">
        Expand on the problem
      </div>
      <div className="flex flex-row justify-center items-center w-84 h-56 bg-white rounded-b-md">
      <img className="w-10 h-12 mx-4" src="../Draw.png"/>
        <div className="mr-5 w-132">
          <div className="py-2 text-xs">
            Show what you’ve tried, tell us what happened, and why it didn’t
            meet your needs.
          </div>
          <div className="py-2 text-xs">
            Not all questions benefit from including code, but if your problem
            is better understood with code you’ve written, you should include a
            minimal, reproducible example.
          </div>
          <div className="py-2 text-xs">
            Please make sure to post code and errors as text directly to the
            question (and not as images), and format them appropriately.
          </div>
        </div>
      </div>
    </div>
  );
}
