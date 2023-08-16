export default function TipTags() {
  return (
    <div className="absolute top-0 left-216 flex flex-col justify-center items-center border border-solid border-gray-300 rounded-md shadow-xss">
      <div className="w-84 h-11 py-2 pl-3 bg-gray-100 border border-solid border-b-gray-300 rounded-t-md">
        Adding tags
      </div>
      <div className="flex flex-row justify-center items-center w-84 h-48 bg-white rounded-b-md">
        <img className="w-10 h-12 mx-4" src="../Draw.png" />
        <div className="mr-5 w-132">
          <div className="py-2 text-xs">
            Tags help ensure that your question will get attention from the
            right people.
          </div>
          <div className="py-2 text-xs">
            Tag things in more than one way so people can find them more easily.
            Add tags for product lines, projects, teams, and the specific
            technologies or languages used.
          </div>
          <div className="py-2 text-xs text-sky-600 hover:text-sky-500 cursor-pointer">
            Learn more about tagging
          </div>
        </div>
      </div>
    </div>
  );
}
