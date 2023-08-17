import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { faStackOverflow } from '@fortawesome/free-brands-svg-icons';
import {
  faCircleQuestion,
  faMedal,
  faWindowRestore,
} from '@fortawesome/free-solid-svg-icons';
import { Link } from 'react-router-dom';
import { RouteConst } from '../../Interface/RouteConst';
import Search from './Search/Search';

import DropdownProducts from '../Dropdown/DropdownProducts';
import DropdownInbox from '../Dropdown/DropdownInbox';
import DropdownAchievements from '../Dropdown/DropdownAchievements';
import DropdownQuestion from '../Dropdown/DropdownQuestion';
import DropdownMenu from '../Dropdown/DropdownMenu';
import { useState } from 'react';

export default function HeaderAfter() {
  const [isClick, setClick] = useState(false);

  const handleDropdown = (el) => {
    if (isClick === el) {
      setClick(false);
    } else {
      setClick(el);
    }
  };

  return (
    <header className="sticky top-0 flex flex-row justify-center items-center h-12 z-10 bg-white border-t-4 border-orange-400 border-b-1 border-b-gray-300">
      <Link
        to={RouteConst.Main}
        className="flex flex-row justify-center items-center w-42 h-11 hover:bg-gray-200 hover:h-11 text-lg"
      >
        <FontAwesomeIcon
          icon={faStackOverflow}
          style={{ color: '#fb9637', fontSize: '2rem' }}
        />
        <span className="ml-1 w-32">
          stack <span className="font-bold">overflow</span>
        </span>
      </Link>
      <button
        className="relative h-6 mx-1 px-3 hover:bg-gray-200 rounded-xl text-xs text-stack"
        onClick={() => {
          handleDropdown(0);
        }}
        onBlur={() => {
          setClick(false);
        }}
      >
        Products
        {isClick === 0 ? <DropdownProducts /> : null}
      </button>
      <Search />
      <Link to={RouteConst.memberMain}>
        <button className="flex flex-row justify-center items-center h-11 px-2 hover:bg-gray-200 hover:h-11">
          <img
            className="w-6 h-6 rounded-md border border-solid border-balck"
            src="" // 나의 프로필 url 주소 작성하기
            alt="profile"
          />
          <span className="mx-1 font-semibold text-xs">4</span>
          <ul className="flex flex-row">
            <li className="mx-1 text-yellow-400 text-xs">● 1</li>
            <li className="mx-1 text-gray-300 text-xs">● 1</li>
            <li className="mx-1 text-yellow-600 text-xs">● 1</li>
          </ul>
        </button>
      </Link>
      <button
        className="relative w-10 h-11 hover:bg-gray-200 hover:h-11"
        onClick={() => {
          handleDropdown(1);
        }}
        onBlur={() => {
          setClick(false);
        }}
      >
        <FontAwesomeIcon icon={faWindowRestore} className="w-4 h-4" />
        {isClick === 1 ? <DropdownInbox /> : null}
      </button>
      <button
        className="relative w-10 h-11 hover:bg-gray-200 hover:h-11"
        onClick={() => {
          handleDropdown(2);
        }}
        onBlur={() => {
          setClick(false);
        }}
      >
        <FontAwesomeIcon icon={faMedal} className="w-4 h-4" />
        {isClick === 2 ? <DropdownAchievements /> : null}
      </button>
      <button
        className="relative w-10 h-11 hover:bg-gray-200 hover:h-11"
        onClick={() => {
          handleDropdown(3);
        }}
        onBlur={() => {
          setClick(false);
        }}
      >
        <FontAwesomeIcon icon={faCircleQuestion} className="w-4 h-4" />
        {isClick === 3 ? <DropdownQuestion /> : null}
      </button>
      <button
        className="relative w-10 h-11 hover:bg-gray-200 hover:h-11"
        onClick={() => {
          handleDropdown(4);
        }}
        onBlur={() => {
          setClick(false);
        }}
      >
        <FontAwesomeIcon icon={faBars} className="w-4 h-4" />
        {isClick === 4 ? <DropdownMenu /> : null}
      </button>
    </header>
  );
}
