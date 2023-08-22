import Pagination from 'react-js-pagination';
import './Paging.css';
import { useState } from 'react';
import { useDispatch } from 'react-redux';
import { filter } from '../../../redux/createSlice/QuestionSlice';

const Paging = () => {
  const [page, setPage] = useState(1);
  const dispatch = useDispatch();

  //   activePage: 현재 페이지
  //   itemsCountPerPage: 한 페이지에 보여줄 아이템 개수
  //   totalItemsCount: 전체 아이템 개수
  //   pageRangeDisplayed: 페이지 범위
  //   prevPageText: 이전 버튼 텍스트
  //   nextPageText: 다음 버튼 텍스트
  //   onChange: 페이지 클릭 시 실행되는 함수

  return (
    <div className="flex justify-center">
      <Pagination
        activePage={page}
        itemsCountPerPage={10}
        totalItemsCount={450}
        pageRangeDisplayed={5}
        prevPageText={'‹'}
        nextPageText={'›'}
        onChange={(page) => {
          setPage(page);
          dispatch(filter({ page: page }));
        }}
      />
    </div>
  );
};

export default Paging;
