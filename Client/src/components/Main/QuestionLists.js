import data from './data.json';
import Paging from './Pagination/Paging';

export default function QuestionList() {
  const limitedData = data.data.slice(0, 8);

  return (
    <>
      {limitedData.map((el) => (
        <div className="w-full flex justify-center">
          <div className="w-[700px] flex justify-between items-center border-t border-t-[#9e9e9e] border-b-1 border-b-[#9e9e9e]">
            {/* 투표수, 답변수, 조회수  */}
            <div className="h-[80px] flex flex-col justify-between">
              <p>{el.votes} votes</p>
              <p className="text-[#90A4AE]">0 answer</p>
              <p className="text-[#90A4AE]">{el.views} views</p>
            </div>

            {/* 컨텐츠 */}
            <div className="w-[600px] h-[100px] flex flex-col justify-around ">
              <a href="/questions" className="text-[#6179c9] text-xl hover:text-[#afdcf7]">{el.title}</a>
              <p className="text-[#616161]">{el.content}</p>
              {/* 태그, 유저, 생성날짜 */}
              <div className="w-full flex justify-between">
                <div className="mr-4">
                  {el.tags.map((tag, index) => (
                    <button key={index} className="w-[100px] bg-[#afdcf7b4] rounded-full mr-4 text-[#95a6dd] hover:translate-x-1 hover:translate-y-1">
                      {tag}
                    </button>
                  ))}
                </div>
                <div className="flex justify-between">
                  <p className="ml-4 text-sm text-[#4FC3F7]">{el.AskUser}</p>
                  <p className="ml-6 text-sm text-[#9e9e9e]">{el.CreatedAt}</p>
                </div>
              </div>
            </div>
          </div>
        </div>
      ))}
      <Paging />
    </>
  );
}
