import { Link } from 'react-router-dom';
import Buttons from '../../../components/Members/Buttons';
import MemberInfo from '../../../components/Members/MemberInfo';
import { RouteConst } from '../../../Interface/RouteConst';

export default function MemberEdit() {
  return (
    <>
      <div className="flex flex-col p-6">
        <MemberInfo />
        <Buttons />
        <div className="flex">
          <div className="flex flex-col text-gray-400 text-sm w-[10rem]">
            <div className="flex">
              <Link
                to={RouteConst.memberEdit}
                className="hover:bg-gray-300 py-1 rounded-2xl px-2"
              >
                Edit profile
              </Link>
            </div>
            <div className="flex">
              <Link
                to={RouteConst.memberDelete}
                className="hover:bg-gray-300 py-1 rounded-2xl px-2"
              >
                Delete profile
              </Link>
            </div>
          </div>
          <div className="w-full pl-[1rem]">
            <div className=" text-xl font-bold pb-4 border-b-[2px] border-gray-200">
              Edit your profile
            </div>
            <div className=" text-xl font-bold py-3 mt-2">
              Public information
            </div>
            <div className="border-[1px] rounded-xl border-black min-h-[40rem] pl-[1rem] pt-4">
              <div className="pb-1">
                <div className=" font-bold">profile image</div>
                <div className="relative">
                  <img
                    src="https://img.freepik.com/free-vector/cute-cat-sitting-cartoon-vector-icon-illustration-animal-nature-icon-concept-isolated-premium-vector-flat-cartoon-style_138676-4148.jpg?q=10&h=200"
                    alt="memberImg"
                    className="w-40 h-40 rounded-xl"
                  />
                  <div className="absolute bottom-0 left-0 right-0 w-40 h-10 hover:bg-opacity-60 hover:cursor-pointer rounded-b-xl bg-black bg-opacity-40 flex items-center justify-center">
                    <span className="text-white">Change picture</span>
                  </div>
                </div>
              </div>
              <div className="py-1">
                <div className=" font-bold">Display name</div>
                <input
                  type="text"
                  className="border-[1px] border-black w-[25rem]"
                  value="귀여운 고양이"
                ></input>
              </div>
              <div className="py-1">
                <div className=" font-bold">Location</div>
                <input
                  type="text"
                  className="border-[1px] border-black w-[25rem]"
                ></input>
              </div>
              <div className="py-1">
                <div className=" font-bold">Title</div>
                <input
                  type="text"
                  className="border-[1px] border-black w-[25rem]"
                ></input>
              </div>
              <div className="py-1">
                <div className=" font-bold">About me</div>
                <textarea className="border-[1px] border-black w-[42rem] h-[10rem]"></textarea>
              </div>
              <button className="w-20 h-8 mx-1 px-2 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white">
                Save profile
              </button>
              <button className="w-16 h-8 mx-1 px-2 bg-sky-100 hover:bg-sky-200 rounded-md text-xs text-sky-600 mb-8">
                Cancel
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
