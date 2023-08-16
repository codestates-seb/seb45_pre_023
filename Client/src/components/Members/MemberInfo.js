import { Link } from "react-router-dom";
import { RouteConst } from "../../Interface/RouteConst";

export default function MemberInfo() {
  return (
    <div className="flex  w-220">
      <img
        src="https://img.freepik.com/free-vector/cute-cat-sitting-cartoon-vector-icon-illustration-animal-nature-icon-concept-isolated-premium-vector-flat-cartoon-style_138676-4148.jpg?q=10&h=200"
        alt="memeberImg" className=" w-40 h-40 rounded-3xl"
      />
      <div className="flex flex-col ml-[1rem] w-[45rem]">
        <div className="flex justify-end">
          <Link to={RouteConst.memberEdit} className=" shadow-lg hover:bg-slate-200 border-black rounded-xl text-sm p-[0.25rem] mr-[2rem]">
            Edit Profile
          </Link>
        </div>
        <div className=" text-3xl mt-7">귀여운 고양이</div>
        <div className="pt-2">
          <span className=" pr-2 text-xs">Member for 2days</span>
          <span className=" pr-2 text-xs">Last seen this week</span>
          <span className="text-xs">visited 2days, 2 consecutive</span>
        </div>
      </div>
    </div>
  );
}
