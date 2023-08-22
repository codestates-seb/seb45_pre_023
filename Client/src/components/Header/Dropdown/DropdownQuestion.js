export default function DropdownQuestion() {
  return (
    <ul className="absolute flex flex-col justify-center items-center w-56 top-11 right-0 bg-white border border-solid border-gray">
      <li className="py-1 px-2 text-left hover:bg-gray-200">
        <div className="font-semibold text-sky-600 text-sm">Tour</div>
        <div className="leading-4 py-1 text-xs">Start here for a quick overview of the site</div>
      </li>
      <li className="py-1 px-2 text-left hover:bg-gray-200 border border-solid border-t-gray">
        <div className="font-semibold text-sky-600 text-sm">Help Center</div>
        <div className="leading-4 py-1 text-xs">Detailed answers to any questions you might have</div>
      </li>
      <li className="py-1 px-2 text-left hover:bg-gray-200 border border-solid border-t-gray">
        <div className="font-semibold text-sky-600 text-sm">Meta</div>
        <div className="leading-4 py-1 text-xs">Dicuss the workings and policies of this site</div>
      </li>
      <li className="py-1 px-2 text-left hover:bg-gray-200 border border-solid border-t-gray">
        <div className="font-semibold text-sky-600 text-sm">About Us</div>
        <div className="leading-4 py-1 text-xs">Learn more about Stack Overflow the company, and our products.</div>
      </li>
    </ul>
  );
}
