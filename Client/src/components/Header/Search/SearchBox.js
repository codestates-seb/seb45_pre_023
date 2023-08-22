export default function SearchBox() {
  return (
    <div className="absolute top-9 right-0 w-168 bg-white border-2 border-solid border-gray rounded-md">
      <ul className="grid grid-cols-2 py-2">
        <li className="w-80 pl-2 py-1">
          <span className="mx-2 text-sm font-semibold">[tag]</span>
          <span className="text-sm text-gray-400">search within a tag</span>
        </li>
        <li className="w-80 pl-2 py-1">
          <span className="mx-2 text-sm font-semibold">answer:0</span>
          <span className="text-sm text-gray-400">unanwered questions</span>
        </li>
        <li className="w-80 pl-2 py-1">
          <span className="mx-2 text-sm font-semibold">user:1234</span>
          <span className="text-sm text-gray-400">serarch by author</span>
        </li>
        <li className="w-80 pl-2 py-1">
          <span className="mx-2 text-sm font-semibold">score:3</span>
          <span className="text-sm text-gray-400">posts with a 3+ score</span>
        </li>
        <li className="w-80 pl-2 py-1">
          <span className="mx-2 text-sm font-semibold">"words here"</span>
          <span className="text-sm text-gray-400">exact pharse</span>
        </li>
        <li className="w-80 pl-2 py-1">
          <span className="mx-2 text-sm font-semibold">is:question</span>
          <span className="text-sm text-gray-400">type of post</span>
        </li>
        <li className="w-80 pl-2 py-1">
          <span className="mx-2 text-sm font-semibold">collective:"Name"</span>
          <span className="text-sm text-gray-400">collective content</span>
        </li>
        <li className="w-80 pl-2 py-1">
          <span className="mx-2 text-sm font-semibold">isaccepted:yes</span>
          <span className="text-sm text-gray-400">search within status</span>
        </li>
      </ul>
      <div className="flex flex-row items-center justify-between py-3 border border-solid border-t-gray-300">
        <button className="ml-3 w-24 h-8 rounded-md text-center bg-sky-100 hover:bg-sky-200 text-sky-600 hover:text-sky-700 text-xs">
          Ask a question
        </button>
        <div className="pr-4 text-sky-600 hover:text-sky-700 text-xs">
          Search help
        </div>
      </div>
    </div>
  );
}
