export default function DropdownQuestion() {
  return (
    <ul className="flex flex-col justify-center items-center w-56 top-12 right-0 absolute border border-solid border-gray">
      <li className="py-1 px-2 text-left hover:bg-gray-200">
        <div className="text-sm">Tour</div>
        <span className="text-xs">Start here for a quick overview of the site</span>
      </li>
      <li className="py-1 px-2 text-left hover:bg-gray-200 border border-solid border-t-gray">
        <div className="text-sm">Help Center</div>
        <span className="text-xs">Detailed answers to any questions you might have</span>
      </li>
      <li className="py-1 px-2 text-left hover:bg-gray-200 border border-solid border-t-gray">
        <div className="text-sm">Meta</div>
        <span className="text-xs">Dicuss the workings and policies of this site</span>
      </li>
      <li className="py-1 px-2 text-left hover:bg-gray-200 border border-solid border-t-gray">
        <div className="text-sm">About Us</div>
        <span className="text-xs">Learn more about Stack Overflow the company, and our products.</span>
      </li>
    </ul>
  );
}
