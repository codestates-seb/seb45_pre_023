export default function FilteringButton() {
  return (
    <>
        <div className = "w-50 flex justify-around ">
          <button className= "w-[70px] h-[40px] rounded-2xl border border-[#E0E0E0] hover:bg-[#bdbdbd]">Newest</button>
          <button className= "w-[70px] h-[40px] rounded-2xl border border-[#E0E0E0] hover:bg-[#bdbdbd]">Active</button>
          <button className= "w-[70px] h-[40px] rounded-2xl border border-[#E0E0E0] hover:bg-[#bdbdbd]">Bountied</button>
          <button className= "w-[100px] h-[40px] rounded-2xl border border-[#E0E0E0] hover:bg-[#bdbdbd]">Unanswered</button>
        </div>
    </>
  );
}
