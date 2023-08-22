export default function DropdownAchievements() {
  return (
    <ul className="absolute flex flex-col items-center w-88 h-88 top-11 right-0 bg-white border border-solid border-gray">
      <li className="flex flex-row justify-between w-88 h-8 px-3 py-2 bg-gray-200">
        <span className="font-bold text-xs">Achievements</span>
        <span className="text-sky-500 text-xs">privileges ● badges</span>
      </li>
      <li className="w-88 h-10 py-3 ml-10 font-bold text-gray-500 text-left text-xs">
        Last 7 days
      </li>
      <li className="flex flex-row justify-center items-center pt-3 pl-11 text-left hover:bg-gray-200">
        <span className="pb-2 text-sky-600 text-xs">
          You've earned the "Autobiographer" badge (Complete "About Me" section
          of user profile).
        </span>
      </li>
    </ul>
  );
}
