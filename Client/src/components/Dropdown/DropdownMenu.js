import { useDispatch } from 'react-redux';
import { logintoken } from '../../redux/createSlice/LoginInfoSlice';
import { oauthtoken } from '../../redux/createSlice/OAuthSlice';
import { RouteConst } from '../../Interface/RouteConst';
import { useNavigate } from 'react-router-dom';

export default function DropdownMenu() {
  const dispatch = useDispatch();
  const nevigate = useNavigate();

  return (
    <ul className="absolute flex flex-col items-center w-88 h-18 top-11 right-0 bg-white border border-solid border-gray-200">
      <li className="flex flex-row justify-between w-88 h-8 px-3 py-2 bg-gray-200">
        <span className="font-bold text-sky-600 text-xs">
          Currnet Community
        </span>
      </li>
      <li className="w-88 h-10 py-3 pl-8 text-sky-500 text-left text-xs">
        <span className="mr-28 font-bold">Stack Overflow</span>
        <span className="hover:text-sky-700">help</span>
        <span className="ml-3 hover:text-sky-700">chat</span>
        <span
          className="ml-3 hover:text-sky-700"
          onClick={() => {
            dispatch(logintoken(''));
            dispatch(oauthtoken(''));
            nevigate(RouteConst.Login);
          }}
        >
          log out
        </span>
      </li>
    </ul>
  );
}
