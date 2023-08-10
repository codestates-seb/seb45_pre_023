import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faBars } from '@fortawesome/free-solid-svg-icons';
import { faStackOverflow } from '@fortawesome/free-brands-svg-icons';
import { faCircleQuestion, faMedal, faWindowRestore } from '@fortawesome/free-solid-svg-icons';
import { Link } from 'react-router-dom';
import { RouteConst } from '../Interface/RouteConst';
import Search from './Search';

export default function HeaderAfter() {
    return (
        <header className="flex flex-row justify-center items-center h-12 border-t-2 border-orange-400 border-b-1 border-b-gray-300">
          <Link to={RouteConst.Login} className="flex flex-row justify-center items-center w-42 h-12 hover:bg-gray-200 text-lg">
            <FontAwesomeIcon icon={faStackOverflow} style={{ color: '#fb9637', fontSize: '2rem' }} />
            <span className="ml-1 w-32">stack <span className='font-bold'>overflow</span></span>
          </Link>
          <button className="h-6 mx-1 px-3 hover:bg-gray-200 rounded-xl text-xs text-stack">Products</button>
          <Search />
          <button className="flex flex-row justify-center items-center h-12 px-2 hover:bg-gray-200">
            <img className='w-6 h-6 rounded-md border border-solid border-balck' src='' alt='profile'/>
            <span className='mx-1 font-semibold text-xs'>4</span>
            <ul className='flex flex-row'>
                <li className='mx-1 text-yellow-400 text-xs'>● 1</li>
                <li className='mx-1 text-gray-300 text-xs'>● 1</li>
                <li className='mx-1 text-yellow-600 text-xs'>● 1</li>
            </ul>
          </button>
          <button className="w-10 h-12 hover:bg-gray-200">
            <FontAwesomeIcon icon={faWindowRestore} className="w-4 h-4"/>
          </button>
          <button className="w-10 h-12 hover:bg-gray-200">
            <FontAwesomeIcon icon={faMedal} className="w-4 h-4"/>
          </button>
          <button className="w-10 h-12 hover:bg-gray-200">
            <FontAwesomeIcon icon={faCircleQuestion} className="w-4 h-4"/>
          </button>
          <button className="w-10 h-12 hover:bg-gray-200">
            <FontAwesomeIcon icon={faBars} className="w-4 h-4"/>
          </button>
        </header>
    )
}