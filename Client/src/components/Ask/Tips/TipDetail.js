export default function TipDetail() {
  return (
    <div className="absolute top-0 left-216 flex flex-col justify-center items-center border border-solid border-gray-300 rounded-md shadow-xss">
      <div className="w-84 h-11 py-2 pl-3 bg-gray-100 border border-solid border-b-gray-300 rounded-t-md">
        Introduce the problem
      </div>
      <div className="flex flex-row justify-center items-center w-84 h-24 bg-white rounded-b-md">
      <img className="w-10 h-12 mx-4" src="../Draw.png"/>
        <div className="mr-5 w-132">
          <div className="py-2 text-xs">
            Explain how you encountered the problem you’re trying to solve, and
            any difficulties that have prevented you from solving it yourself.
          </div>
        </div>
      </div>
    </div>
  );
}
