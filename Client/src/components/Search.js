import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faMagnifyingGlass } from '@fortawesome/free-solid-svg-icons';

export default function Search() {
  return (
    <div className='flex felx-row items-center px-2 mx-1 w-168 h-8 rounded-md border-2 border-solid border-gray'>
      <FontAwesomeIcon icon={faMagnifyingGlass} style={{color: "#c7c7c7"}} />
      <input className='ml-2 w-132 h-6 text-sm' placeholder="Search..."></input>
    </div>
  );
}
