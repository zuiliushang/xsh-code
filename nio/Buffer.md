# Buffer类

特定类型数据的容器。

Buffer是特定基元类型元素的线性、有限序列。除了数据内容，一个buffer还有一些基本的属性:容量(capacity)、界限(limit)、位置(position)

- buffer的容量是指包含元素的数量。从不为负，也从不改变。
- buffer的界限是指相对于第一个元素的位置的不能被读写的元素。这个不过为负也不会大于容量。
- buffer的位置是指下一个被读写的元素的位置。不为负也不会大于界限。

## 传输数据

byte的子类定义了put和get两种类型的操作:

- 相对操作的read和write从当前位置开始。然后通过传输的数量来添加位置。如果读写超过limit。那么抛出BufferUnderflowException异常。
- 绝对操作输入一个明确的index开始。和position没关系。如果超过limit 那么抛出BufferUnderflowException异常。

## 标记和重置

Buffer的标记位置是为了reset方法被调用的时候重置的位置。```mark```它并不是必须被定义的。但是被定义的时候不会为负数并且不超过potition。当定义的时候大于position或者limit则定义失效(被抛弃)。

## 一般规则

mark,position,limit和capacity的值的关系:

```0<=mark<=position<=limit<=capacity```

新创建的Buffer的position为0并且mark未定义。初始化的limit可能为0或者在构造器中定义了值。新分配的缓冲区的每个元素都初始化为零。

## Clearing,flipping,rewinding

除了访问potition,limit和capacity的值的方法和marking,resetting之外。buffer还定义了其他方法来操作buffer:

- clear() 让buffer准备接受一些列的```channel-read```或相对```put```操作:它将limit设置到capacity的位置并且将position限制为0。
- flip() 让buffer准备接受一系列的```channel-write```或相对```get```操作:它将limit设置到当前position诶之并且设置position为0。
- rewind()让buffer准备重新读取数据(即使已经存在内容):limit保持不变,position设置为0。

## 只读buffer

每个buffer都是可读的,但是并不是每个都是可写的。如果在一个只读的buffer上进行变化的操作时会抛出一个```ReadOnlyBufferException```异常。只读buffer虽然不允许内容变化。但是它的mark、position和limit的值是可以变得。调用```isReadOnly```方法可以将一个buffer设置成只读。

## 线程安全

Buffer并不是线程安全。
