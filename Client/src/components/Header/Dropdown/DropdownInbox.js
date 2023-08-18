export default function DropdownInbox() {
  return (
    <ul className="absolute flex flex-col items-center w-88 h-88 top-11 right-0 bg-white border border-solid border-gray">
      <li className="flex flex-row justify-between w-88 h-8 px-3 py-2 bg-gray-200">
        <span className="font-bold text-xs">INBOX (ALL)</span>
        <span className="text-sky-500 text-xs">Mark all as read</span>
      </li>
      <li className="flex flex-col justify-center py-1 px-6 text-left bg-gray-50 hover:bg-gray-100">
        <div className="py-1 pl-1 text-xs">welcome</div>
        <span className="pb-2 text-xs">
          Welcome to Stack Overflow! Take the 2-minute site tour to earn your
          first badge.
        </span>
      </li>
    </ul>
  );
}
