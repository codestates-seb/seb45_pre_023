import { Link } from 'react-router-dom';
import Buttons from '../../../components/Members/Buttons';
import MemberInfo from '../../../components/Members/MemberInfo';
import { RouteConst } from '../../../Interface/RouteConst';
import RightSidebar from '../../../components/SideBar/RightSidebar';
import LeftSidebar from '../../../components/SideBar/LeftSidebar';
import axios from 'axios';
import { useSelector } from 'react-redux';
import { useState } from 'react';

export default function MemberEdit() {
  const memberId = useSelector((state) => state.logininfo.myid); //member Id값 불러오기
  const nickName = useSelector((state) => state.memberinfo.value.nickname);
  const locations = useSelector((state) => state.memberinfo.value.location);
  const title = useSelector((state) => state.memberinfo.value.title);
  const myIntro = useSelector((state) => state.memberinfo.value.myIntro);
  const account = useSelector((state) => state.memberinfo.value.account);
  const token = useSelector((state) => state.logininfo.token);

  const [name, setName] = useState(nickName);
  const [locate, setLocate] = useState(locations);
  const [Title, SetTitle] = useState(title);
  const [intro, setIntro] = useState(myIntro);
  const [myAccount, setMyAccount] = useState(account);

  const handleText = (e) => {
    const { name, value } = e.target;
    switch (name) {
      case 'nickname':
        setName(value);
        break;
      case 'location':
        setLocate(value);
        break;
      case 'title':
        SetTitle(value);
        break;
      case 'myIntro':
        setIntro(value);
        break;
      case 'account':
        setMyAccount(value);
        break;
      default:
        break;
    }
  };
  const data = {
    nickname: name,
    myIntro: intro,
    title: Title,
    accounts: [myAccount],
    location: locate,
  };
  const changeInfo = () => {
    console.log(data);
    axios
      .patch(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/members/${memberId}`,
        data,
        {
          headers: {
            Authorization: token,
          },
        }
      )
      .then((res) => alert('프로필이 변경 되었습니다.'))
      .then(window.location.reload())
      .catch((err) => console.log(err));
  };

  return (
    <div className="flex min-h-[70rem]">
      <LeftSidebar />
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
                  name="nickname"
                  value={name}
                  onChange={handleText}
                ></input>
              </div>
              <div className="py-1">
                <div className=" font-bold">Location</div>
                <input
                  type="text"
                  className="border-[1px] border-black w-[25rem]"
                  name="location"
                  value={locate}
                  onChange={handleText}
                ></input>
              </div>
              <div className="py-1">
                <div className=" font-bold">Title</div>
                <input
                  type="text"
                  className="border-[1px] border-black w-[25rem]"
                  name="title"
                  value={Title}
                  onChange={handleText}
                ></input>
              </div>
              <div className="py-1">
                <div className=" font-bold">Account</div>
                <input
                  type="text"
                  className="border-[1px] border-black w-[25rem]"
                  name="account"
                  value={myAccount}
                  onChange={handleText}
                ></input>
              </div>
              <div className="py-1">
                <div className=" font-bold">About me</div>
                <textarea
                  className="border-[1px] border-black w-[42rem] h-[10rem]"
                  name="myIntro"
                  value={intro}
                  onChange={handleText}
                ></textarea>
              </div>
              <button
                className="w-20 h-8 mx-1 px-2 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
                onClick={changeInfo}
              >
                Save profile
              </button>
              <button className="w-16 h-8 mx-1 px-2 bg-sky-100 hover:bg-sky-200 rounded-md text-xs text-sky-600 mb-8">
                <Link to={RouteConst.memberMain}>Cancel</Link>
              </button>
            </div>
          </div>
        </div>
      </div>
      <RightSidebar />
    </div>
  );
}
