import { Link } from "react-router-dom";
import { RouteConst } from "../../Interface/RouteConst";

export default function LeftSidebar() {
  return (
    <div className="left-sidebar w-40 ml-28">
      <nav className="flex flex-col w-40 border-t-2 border-t-blue-500 border-b-2 border-b-red-400 sticky top-12">
        <div className="py-2 font-bold">Home</div>
        <div className=" text-xs p-1 pt-4 decoration-gray-500">PUBLIC</div>
        <ul className="py-1 text-xs pl-4 decoration-gray-500">
          <Link to={RouteConst.Question} className="py-1">Questions</Link>
          <li className="py-1">Tags</li>
          <li className="py-1">Users</li>
          <li className="py-1">Companies</li>
        </ul>
        <div className="text-xs p-1 pt-4 decoration-gray-500">COLLECTIVES</div>
        <ul className="py-1 text-xs pl-4 decoration-gray-500">
          <li>Explore Collectives</li>
        </ul>
        <div className="text-xs p-1 pt-4 decoration-gray-500">TEAMS</div>
        <ul className="py-1 text-xs pl-4 decoration-gray-500">
          <li>Create free Team</li>
        </ul>
        <div className=" text-xs p-1 pt-4 decoration-gray-500">Looking for your Teams??</div>
      </nav>
    </div>
  );
}
