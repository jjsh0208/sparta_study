package com.example.locking.item;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ItemService {

	private final ItemRepository itemRepository;

	@Transactional
	public void updateItemQuantity(Long id, Integer newQuantity){
		Item item = itemRepository.findByIdWithLock(id);

		item.setQuantity(newQuantity);

		itemRepository.save(item);

	}

	@Transactional(timeout = 1, isolation = Isolation.SERIALIZABLE)
	public Item findById(Long id){
		return itemRepository.findById(id).orElse(null);
	}


	public void updateItemsQuantity(Long itemId1, Long itemId2){
		Item item1 = itemRepository.findByIdWithLock(itemId1);
		item1.setQuantity(item1.getQuantity() + 10);
		itemRepository.save(item1);


		try{
			Thread.sleep(4000);
		}catch (InterruptedException e){
			e.printStackTrace();
		}

		Item item2 = itemRepository.findByIdWithLock(itemId2);
		item2.setQuantity(item2.getQuantity() + 10);
		itemRepository.save(item2);

	}

}
