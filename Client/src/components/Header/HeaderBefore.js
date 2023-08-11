import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { faStackOverflow } from '@fortawesome/free-brands-svg-icons';
import { Link } from 'react-router-dom';
import { RouteConst } from '../../Interface/RouteConst';
import Search from './Search/Search';

export default function HeaderBefore() {
    return (
        <header className="flex flex-row justify-center items-center h-12 border-t-4 border-orange-400 border-b-1 border-b-gray-300">
          <button className="w-12 h-12 hover:bg-gray-200">
            <FontAwesomeIcon icon={faBars} className="w-4 h-4"/>
          </button>
          <Link to={RouteConst.Login} className="flex flex-row justify-center items-center w-42 h-12 hover:bg-gray-200 text-lg">
            <FontAwesomeIcon icon={faStackOverflow} style={{ color: '#fb9637', fontSize: '2rem' }} />
            <span className="ml-1 w-32">stack <span className='font-bold'>overflow</span></span>
          </Link>
          <button className="h-6 mx-1 px-3 hover:bg-gray-200 rounded-xl text-xs text-stack">About</button>
          <button className="h-6 mx-1 px-3 hover:bg-gray-200 rounded-xl text-xs text-stack">Products</button>
          <button className="h-6 mx-1 px-3 hover:bg-gray-200 rounded-xl text-xs text-stack">For Teams</button>
          <Search />
          <button className="w-14 h-8 mx-1 px-2 bg-sky-100 hover:bg-sky-600 rounded-md text-xs">Log in</button>
          <button className="w-16 h-8 mx-1 px-2 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white">Sign up</button>
        </header>
    )
}