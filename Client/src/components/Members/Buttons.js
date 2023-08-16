import { Link } from 'react-router-dom';
import { RouteConst } from '../../Interface/RouteConst';

export default function Buttons() {
  
  return (
    <>
      <div className="flex justify-between w-60 pt-4 mb-4 text-sm text-gray-400">
        <Link to={RouteConst.memberProfile} className='hover:bg-gray-300 rounded-md px-2 '>Profile</Link>
        <Link to={RouteConst.memberMain} className='hover:bg-gray-300 rounded-md px-2'>Activity</Link>
        <Link to={RouteConst.memberEdit} className='hover:bg-gray-300 rounded-md px-2'>Settings</Link>
      </div>
    </>
  );
}
