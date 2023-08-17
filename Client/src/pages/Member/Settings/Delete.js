import { Link } from 'react-router-dom';
import Buttons from '../../../components/Members/Buttons';
import MemberInfo from '../../../components/Members/MemberInfo';
import { RouteConst } from '../../../Interface/RouteConst';

export default function MemberDelete() {
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
          <div className="px-5">
            <div className=" text-2xl font-bold py-4 border-b-[2px] border-black">
              Delete Profile
            </div>
            <div className=" text-sm pt-[2rem]">
              <text>
                Before confirming that you would like your profile deleted, we`d
                like to take a moment to explain the implications of deletion:
                <br />
                <br />
              </text>
              <text>
                <li>
                  Deletion is irreversible, and you will have no way to regain
                  any of your original content, should this deletion be carried
                  out
                  <br />
                  and you change your mind later on.
                </li>
                <li>
                  Your questions and answers will remain on the site, but will
                  be disassociated and anonymized <br />
                  (the author will be listed as "user22355262") and will not
                  indicate your authorship even if you later return to the site.
                  <br />
                  <br />
                </li>
              </text>
              <text>
                Confirming deletion will only delete your profile on Stack
                Overflow - it will not affect any of your other profiles on the
                <br />
                Stack Exchange network. If you want to delete multiple profiles,
                you'll need to visit each site separately and request deletion
                <br />
                of those individual profiles.
                <br />
                <br />
                <br />
              </text>
              <div className='flex'>
                <input type="checkbox" className='mr-4'></input>
                <text>
                  {' '}
                  I have read the information stated above and understand the
                  implications of having my profile deleted.  I wish to
                  proceed <br />with the deletion of my profile.
                </text>
              </div>
              <button className="w-[8rem] h-8 mx-1 px-2 bg-red-600 hover:bg-red-700 rounded-md text-xs text-white mt-10">
                Delete Profile
              </button>
            </div>
          </div>
        </div>
      </div>
    </>
  );
}
