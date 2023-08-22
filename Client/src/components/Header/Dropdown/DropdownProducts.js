export default function DropdownProducts () {
    return (
        <ul className="flex flex-col justify-center items-center top-8 -left-20 z-10 w-56 rounded-md bg-white bg-white absolute border border-solid border-gray">
          <li className="w-52 py-1 px-2 my-2 rounded-md text-left hover:bg-gray-300">
            <div className="text-sm">Stack Overflow</div>
            <span className="text-gray-400 text-xs">Public questions & answers</span>
          </li>
          <li className="w-52 py-1 px-2 my-2 text-left rounded-md hover:bg-gray-300">
            <div className="text-sm">Stack Overflow for Teams</div>
            <span className="text-gray-400 text-xs">Where develops & technologists share private knowledge with coworkers</span>
          </li>
          <li className="w-52 py-1 px-2 my-2 text-left rounded-md hover:bg-gray-300">
            <div className="text-sm">Talent</div>
            <span className="text-gray-400 text-xs">Build your employer brand</span>
          </li>
          <li className="w-52 py-1 px-2 my-2 text-left rounded-md hover:bg-gray-300">
            <div className="text-sm">Advertising</div>
            <span className="text-gray-400 text-xs">Reach developers & hechnologists worldwide</span>
          </li>
          <li className="w-52 py-1 px-2 my-2 text-left rounded-md hover:bg-gray-300">
            <div className="text-sm">Labs</div>
            <span className="text-gray-400 text-xs">The future of collectiove knowledge sharing</span>
          </li>
          <li className="w-56 h-10 py-2 px-3 rounded-b-md text-left text-sm text-gray-400 hover:text-gray-600 bg-gray-50 border border-solid border-t-gray">About the company</li>
        </ul>
      );
}