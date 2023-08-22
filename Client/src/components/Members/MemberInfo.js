import { Link } from 'react-router-dom';
import { RouteConst } from '../../Interface/RouteConst';
import { useSelector } from 'react-redux';
import { faLocationDot } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

export default function MemberInfo() {
  const nickName = useSelector((state) => state.memberinfo.value.nickname);
  const location = useSelector((state)=> state.memberinfo.value.location)
  const title = useSelector((state)=>state.memberinfo.value.title)
  return (
    <div className="flex  w-220">
      <img
        src="https://img.freepik.com/free-vector/cute-cat-sitting-cartoon-vector-icon-illustration-animal-nature-icon-concept-isolated-premium-vector-flat-cartoon-style_138676-4148.jpg?q=10&h=200"
        alt="memeberImg"
        className=" w-40 h-40 rounded-3xl"
      />
      <div className="flex flex-col ml-[1rem] w-[45rem]">
        <div className="flex justify-end">
          <Link
            to={RouteConst.memberEdit}
            className=" shadow-lg hover:bg-slate-200 border-black rounded-xl text-sm p-[0.25rem] mr-[2rem]"
          >
            Edit Profile
          </Link>
        </div>
        <div className="text-4xl mt-[-1.5rem]">{nickName}</div>
        <div className="pt-2 text-gray-500 text-lg">{title}</div>
        <div className='pt-2 mt-2'>
          <span className=" pr-2 text-xs text-gray-500">Member for 2days</span>
          <span className=" pr-2 text-xs text-gray-500">Last seen this week</span>
          <span className="text-xs text-gray-500">visited 2days, 2 consecutive</span>
        </div>
        <div className='text-gray-500'>
          <FontAwesomeIcon icon={faLocationDot} />
          <span className='pl-2'>{location}</span>
        </div>
      </div>
    </div>
  );
}
